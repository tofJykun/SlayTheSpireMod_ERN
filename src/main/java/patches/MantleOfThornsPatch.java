package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import powers.HoverPower;
import powers.MantleOfThornsPower;

@SpirePatch(clz = ApplyPowerAction.class, method = "update")
public class MantleOfThornsPatch {
    @SpireInsertPatch(locator = PlayerDebuffStatsPatch.SuccessLocator.class)
    public static void insert(ApplyPowerAction __instance, AbstractPower ___powerToApply) {
        if (!(__instance.target instanceof com.megacrit.cardcrawl.characters.AbstractPlayer)
                || !(__instance.target.isPlayer)
                || !(___powerToApply instanceof HoverPower)
                || __instance.amount <= 0) {
            return;
        }

        AbstractPower mantle = __instance.target.getPower(MantleOfThornsPower.POWER_ID);
        if (mantle instanceof MantleOfThornsPower && mantle.amount > 0) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(__instance.target, __instance.target,
                    new ThornsPower(__instance.target, mantle.amount), mantle.amount,
                    AbstractGameAction.AttackEffect.NONE));
        }
    }
}
