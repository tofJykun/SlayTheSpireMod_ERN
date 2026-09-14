package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import powers.TaxesPower;

public class TaxesFreeToPlayPatch {
    @SpirePatch(clz = AbstractCard.class, method = "freeToPlay")
    public static class FreeToPlayPatch {
        @SpirePostfixPatch
        public static boolean postfix(boolean __result, AbstractCard __instance) {
            return __result || TaxesPower.shouldMakeFree(__instance);
        }
    }
}
