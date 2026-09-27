package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.random.Random;
import relics.VoiceConduit;

@SpirePatch(clz = EventHelper.class, method = "roll", paramtypez = {Random.class})
public class VoiceConduitRoomPatch {
    @SpirePostfixPatch
    public static EventHelper.RoomResult postfix(EventHelper.RoomResult __result) {
        if (__result == EventHelper.RoomResult.MONSTER && AbstractDungeon.player != null) {
            VoiceConduit conduit = (VoiceConduit)AbstractDungeon.player.getRelic(VoiceConduit.ID);
            if (conduit != null && conduit.protectsRoomRoll()) {
                return EventHelper.RoomResult.EVENT;
            }
        }
        return __result;
    }
}
