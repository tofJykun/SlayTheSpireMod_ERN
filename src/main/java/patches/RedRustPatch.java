package patches;

import actions.RedRustDuplicateAction;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CombatState;
import powers.RedRustPower;

@SpirePatch(clz = ApplyPowerAction.class, method = "update")
public class RedRustPatch {
    @SpireInsertPatch(locator = PlayerDebuffStatsPatch.SuccessLocator.class)
    public static void insert(ApplyPowerAction __instance, AbstractPower ___powerToApply) {
        if (__instance instanceof RedRustDuplicateAction || !CombatState.isInCombat()
                || !(__instance.target instanceof AbstractMonster) || ___powerToApply == null
                || ___powerToApply.type != AbstractPower.PowerType.DEBUFF || __instance.amount == 0
                || (__instance.amount < 0 && !___powerToApply.canGoNegative)) return;
        AbstractMonster target = (AbstractMonster)__instance.target;
        if (target.isDeadOrEscaped() || target.halfDead || target.currentHealth <= 0) return;
        AbstractPower rust = target.getPower(RedRustPower.POWER_ID);
        // On initial application the new power is already in the list at this locator.
        if (rust instanceof RedRustPower && rust != ___powerToApply && rust.amount > 0) {
            ((RedRustPower)rust).duplicateApplication(___powerToApply, __instance.source, __instance.amount);
        }
    }
}
