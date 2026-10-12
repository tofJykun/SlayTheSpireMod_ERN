package general;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.WingBoots;
import com.megacrit.cardcrawl.rooms.*;
import patches.HermesBootsPatch;
import relics.Lordvessel;

public final class LordvesselRoutes {
    private LordvesselRoutes() {}

    public static Lordvessel relic() {
        AbstractPlayer player = AbstractDungeon.player;
        AbstractRelic relic = player == null ? null : player.getRelic(Lordvessel.ID);
        return relic instanceof Lordvessel ? (Lordvessel) relic : null;
    }

    public static boolean available() {
        Lordvessel relic = relic();
        return relic != null && relic.counter > 0;
    }

    public static boolean isMapRoom(MapRoomNode node) {
        if (node == null || node.room == null || node.y < 0
                || node.room instanceof NeowRoom || node.room instanceof EmptyRoom
                || node.room instanceof TreasureRoomBoss || node.room instanceof VictoryRoom
                || node.room instanceof TrueVictoryRoom) return false;
        if (node.room instanceof MonsterRoomBoss) return true;
        return AbstractDungeon.map != null && node.y < AbstractDungeon.map.size()
                && AbstractDungeon.map.get(node.y).contains(node);
    }

    public static boolean canReach(MapRoomNode from, MapRoomNode to) {
        return available() && from != null && from != to && isMapRoom(to)
                && !(from.room instanceof MonsterRoomBoss && to.room instanceof MonsterRoomBoss);
    }

    public static boolean normalBossRoute() {
        MapRoomNode from = AbstractDungeon.getCurrMapNode();
        return from != null && ("TheEnding".equals(AbstractDungeon.id) ? from.y == 2 : from.y == 14);
    }

    public static boolean canReachBoss() {
        MapRoomNode from = AbstractDungeon.getCurrMapNode();
        return available() && from != null && !(from.room instanceof MonsterRoomBoss)
                && !(from.room instanceof TreasureRoomBoss) && !(from.room instanceof VictoryRoom);
    }

    // True means let the native confirmed-selection branch decrement Wing Boots.
    public static boolean consumeOffRoute(AbstractPlayer player, String id, MapRoomNode to) {
        if (!WingBoots.ID.equals(id)) return player != null && player.hasRelic(id);
        MapRoomNode from = AbstractDungeon.getCurrMapNode();
        if (from != null && to != null && to.y == from.y + 1) {
            if (!AbstractDungeon.firstRoomChosen && to.y == 0) return false;
            if (HermesBootsPatch.canFly(from, to)) {
                HermesBootsPatch.consumeHermesOrUseWingBoots(player, id);
                return false;
            }
            AbstractRelic wing = player == null ? null : player.getRelic(id);
            if (wing != null && wing.counter > 0) return true;
        }
        if (canReach(from, to)) {
            LordvesselHistory.recordJump(from, to);
            relic().consumeCharge();
            AbstractDungeon.firstRoomChosen = true;
            return false;
        }
        return false;
    }
}
