package patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.CovetousSilverSerpentRing;

public class CovetousSilverSerpentRingGoldPatch {
    private static final float GOLD_MULTIPLIER = 1.25F;

    @SpirePatch(clz = AbstractPlayer.class, method = "gainGold", paramtypez = {int.class})
    public static class GainGoldPatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractPlayer __instance, @ByRef int[] amount) {
            if (__instance == null || amount == null || amount.length == 0 || amount[0] <= 0) {
                return;
            }
            if (!__instance.hasRelic(CovetousSilverSerpentRing.ID)) {
                return;
            }
            AbstractRelic relic = __instance.getRelic(CovetousSilverSerpentRing.ID);
            if (relic != null) {
                relic.flash();
            }
            amount[0] = (int)Math.ceil(amount[0] * GOLD_MULTIPLIER);
        }
    }
}
