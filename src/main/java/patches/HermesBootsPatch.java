package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.WingBoots;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import relics.HermesBoots;

public class HermesBootsPatch {
    private static HermesBoots boots(AbstractPlayer player) {
        AbstractRelic relic = player == null ? null : player.getRelic(HermesBoots.ID);
        return relic instanceof HermesBoots ? (HermesBoots)relic : null;
    }

    public static boolean canFly(MapRoomNode from, MapRoomNode to) {
        HermesBoots boots = boots(AbstractDungeon.player);
        if (boots == null || boots.counter <= 0 || from == null || to == null) return false;
        for (MapEdge edge : from.getEdges()) {
            if (to.y == edge.dstY) return true;
        }
        return false;
    }

    // Called only inside the native confirmed, off-route selection branch.
    public static boolean consumeHermesOrUseWingBoots(AbstractPlayer player, String relicId) {
        if (WingBoots.ID.equals(relicId)) {
            HermesBoots boots = boots(player);
            if (boots != null && boots.consumeCharge()) return false;
        }
        return player != null && player.hasRelic(relicId);
    }

    @SpirePatch(clz = MapRoomNode.class, method = "wingedIsConnectedTo")
    public static class Connection {
        @SpirePostfixPatch
        public static boolean postfix(boolean __result, MapRoomNode __instance, MapRoomNode node) {
            return __result || canFly(__instance, node);
        }
    }

    @SpirePatch(clz = MapRoomNode.class, method = "update")
    public static class Consume {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall call) throws CannotCompileException {
                    if (call.getClassName().equals(AbstractPlayer.class.getName())
                            && call.getMethodName().equals("hasRelic")) {
                        call.replace("{ $_ = patches.HermesBootsPatch.consumeHermesOrUseWingBoots($0, $1); }");
                    }
                }
            };
        }
    }
}
