package patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import general.LordvesselRoutes;
import general.LordvesselHistory;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import java.util.ArrayList;

public class LordvesselMapPatch {
    private static ArrayList<LordvesselHistory.Jump> savedJumps;
    private static int renderedJumpCount = -1;
    private static String renderedAct;
    private static final ArrayList<MapEdge> usedEdges = new ArrayList<>();

    @SpirePatch(clz = MapRoomNode.class, method = "oscillateColor")
    public static class RevisitHighlight {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess field) throws CannotCompileException {
                    if (field.isReader() && field.getClassName().equals(MapRoomNode.class.getName())
                            && field.getFieldName().equals("taken")) {
                        // Only opt eligible revisits into the native pulse; never change visit history.
                        field.replace("{ $_ = $proceed() && !patches.LordvesselMapPatch.highlightVisitedNode(this); }");
                    }
                }
            };
        }
    }

    public static boolean highlightVisitedNode(MapRoomNode node) {
        MapRoomNode from = AbstractDungeon.getCurrMapNode();
        return node != null && node.taken && LordvesselHistory.hasBacktrackedInAct()
                && from != null && node.y == from.y + 1
                && from.room != null && from.room.phase == AbstractRoom.RoomPhase.COMPLETE
                && (from.isConnectedTo(node) || from.wingedIsConnectedTo(node));
    }

    public static Color revisitColor(MapRoomNode node, Color color) {
        return color == MapRoomNode.AVAILABLE_COLOR && highlightVisitedNode(node) ? node.color : color;
    }

    @SpirePatch(clz = MapRoomNode.class, method = "render")
    public static class RevisitColor {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall call) throws CannotCompileException {
                    if (call.getClassName().equals(SpriteBatch.class.getName())
                            && call.getMethodName().equals("setColor")
                            && call.getSignature().equals("(Lcom/badlogic/gdx/graphics/Color;)V")) {
                        // Preserve both taken checks and all edge drawing. Icon and circle pulse together.
                        call.replace("{ $proceed(patches.LordvesselMapPatch.revisitColor(this, $1)); }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = DungeonMap.class, method = "update")
    public static class BossSelection {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall call) throws CannotCompileException {
                    if (call.getClassName().equals(AbstractDungeon.class.getName())
                            && call.getMethodName().equals("nextRoomTransitionStart")) {
                        call.replace("{ patches.LordvesselMapPatch.startBossTransition(); }");
                    }
                }

                @Override
                public void edit(FieldAccess field) throws CannotCompileException {
                    if (field.isReader() && field.getClassName().equals(MapRoomNode.class.getName())
                            && field.getFieldName().equals("y")) {
                        field.replace("{ $_ = general.LordvesselRoutes.canReachBoss() ? 14 : $proceed(); }");
                    }
                }
            };
        }

        @SpirePostfixPatch
        public static void postfix(DungeonMap __instance, Color ___bossNodeColor) {
            if (LordvesselRoutes.canReachBoss() && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMPLETE) {
                float alpha = ___bossNodeColor.a;
                ___bossNodeColor.set(MapRoomNode.AVAILABLE_COLOR);
                ___bossNodeColor.a = alpha;
            }
        }
    }

    // Only the map icon calls this; Secret Portal and the A20 second Boss remain free.
    public static void startBossTransition() {
        MapRoomNode to = AbstractDungeon.nextRoom;
        if (to != null && to.room instanceof MonsterRoomBoss && !LordvesselRoutes.normalBossRoute()
                && LordvesselRoutes.canReachBoss()) {
            LordvesselHistory.recordJump(AbstractDungeon.getCurrMapNode(), to);
            LordvesselRoutes.relic().consumeCharge();
            AbstractDungeon.firstRoomChosen = true;
            int last = AbstractDungeon.pathY.size() - 1;
            if (last >= 0) {
                AbstractDungeon.pathX.set(last, 1);
                AbstractDungeon.pathY.set(last, 15);
            }
        }
        AbstractDungeon.nextRoomTransitionStart();
    }

    @SpirePatch(clz = DungeonMap.class, method = "render")
    public static class Connections {
        @SpirePostfixPatch
        public static void postfix(DungeonMap __instance, SpriteBatch sb) {
            if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MAP) return;
            String act = AbstractDungeon.actNum + ":" + AbstractDungeon.id;
            if (savedJumps != LordvesselHistory.jumps() || renderedJumpCount != LordvesselHistory.jumps().size()
                    || !act.equals(renderedAct)) {
                savedJumps = LordvesselHistory.jumps();
                renderedJumpCount = savedJumps.size();
                renderedAct = act;
                usedEdges.clear();
                for (LordvesselHistory.Jump jump : savedJumps) {
                    if (!act.equals(jump.act)) continue;
                    float toOffsetX = jump.toOffsetX, toOffsetY = jump.toOffsetY;
                    int toX = jump.toX, toY = jump.toY;
                    if (jump.boss) {
                        toX = 3;
                        toY = 15;
                        toOffsetX = bossOffsetX();
                        toOffsetY = bossOffsetY();
                    }
                    MapEdge edge = new MapEdge(jump.fromX, jump.fromY, jump.fromOffsetX, jump.fromOffsetY,
                            toX, toY, toOffsetX, toOffsetY, jump.boss);
                    edge.markAsTaken();
                    usedEdges.add(edge);
                }
            }
            for (MapEdge edge : usedEdges) edge.render(sb);
        }
    }

    private static float bossOffsetX() {
        float spaceX = Settings.isMobile ? 140.8f * Settings.xScale : 128f * Settings.xScale;
        return AbstractDungeon.dungeonMapScreen.map.bossHb.cX - MapRoomNode.OFFSET_X - 3 * spaceX;
    }

    private static float bossOffsetY() {
        return AbstractDungeon.dungeonMapScreen.map.bossHb.cY - DungeonMapScreen.offsetY
                - 180f * Settings.scale - 15 * Settings.MAP_DST_Y;
    }
}
