package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import powers.RepeatingCrossbowPower;

public class RepeatingCrossbowCostPatch {
    @SpirePatch(clz = AbstractCard.class, method = "freeToPlay")
    public static class FreeToPlay {
        @SpirePostfixPatch
        public static boolean postfix(boolean __result, AbstractCard __instance) {
            return __result || RepeatingCrossbowPower.makesFree(__instance);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "getCost")
    public static class DisplayCost {
        @SpirePostfixPatch
        public static String postfix(String __result, AbstractCard __instance) {
            return RepeatingCrossbowPower.makesFree(__instance) ? "0" : __result;
        }
    }
}
