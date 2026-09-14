package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import powers.HornetRingPower;

public class HornetRingDamagePatch {
    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage",
            paramtypez = { AbstractMonster.class })
    public static class CalculateCardDamagePatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractCard __instance, AbstractMonster mo) {
            if (mo == null || __instance.target == AbstractCard.CardTarget.ALL_ENEMY) {
                HornetRingPower.modifyCardMultiDamage(__instance);
            } else {
                HornetRingPower.modifyCardDamage(__instance, mo);
            }
        }
    }

    @SpirePatch(clz = DamageInfo.class, method = "applyPowers",
            paramtypez = { AbstractCreature.class, AbstractCreature.class })
    public static class ApplyPowersPatch {
        @SpirePostfixPatch
        public static void Postfix(DamageInfo __instance, AbstractCreature owner, AbstractCreature target) {
            HornetRingPower.modifyDamage(__instance, owner, target);
        }
    }
}
