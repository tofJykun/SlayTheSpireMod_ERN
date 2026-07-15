package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import powers.FaithPower;
import powers.IntelligencePower;

public class IntelligenceMagicNumberPatch {
    @SpirePatch(clz = AbstractCard.class, method = "applyPowers")
    public static class ApplyPowersPatch {
        @SpirePostfixPatch
        public static void postfix(AbstractCard __instance) {
            IntelligencePower.applyMagicNumber(__instance);
            FaithPower.applyMagicNumber(__instance);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class CalculateCardDamagePatch {
        @SpirePostfixPatch
        public static void postfix(AbstractCard __instance, AbstractMonster mo) {
            IntelligencePower.applyMagicNumber(__instance);
            FaithPower.applyMagicNumber(__instance);
        }
    }
}
