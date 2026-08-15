package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import powers.CeruleanwhorlBubblePower;

public class CeruleanwhorlBubbleEnergyPatch {
    @SpirePatch(clz = AbstractCard.class, method = "hasEnoughEnergy")
    public static class HasEnoughEnergyPatch {
        @SpirePostfixPatch
        public static boolean postfix(boolean __result, AbstractCard __instance) {
            if (__result) {
                return true;
            }
            return CeruleanwhorlBubblePower.canPlayThroughBubble(__instance);
        }
    }
}
