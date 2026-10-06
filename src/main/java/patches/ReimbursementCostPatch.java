package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import powers.ReimbursementPower;

public class ReimbursementCostPatch {
    @SpirePatch(clz = AbstractCard.class, method = "freeToPlay")
    public static class FreeToPlay {
        @SpirePostfixPatch
        public static boolean postfix(boolean __result, AbstractCard __instance) {
            return __result || ReimbursementPower.makesFree(__instance);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "getCost")
    public static class DisplayCost {
        @SpirePostfixPatch
        public static String postfix(String __result, AbstractCard __instance) {
            return ReimbursementPower.makesFree(__instance) ? "0" : __result;
        }
    }
}
