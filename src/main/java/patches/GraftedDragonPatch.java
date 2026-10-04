package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.AbstractSummonPower;
import powers.GraftedDragonPower;

@SpirePatch(clz = ApplyPowerAction.class, method = "update")
public class GraftedDragonPatch {
    @SpirePrefixPatch
    public static SpireReturn<Void> prefix(ApplyPowerAction __instance, AbstractPower ___powerToApply) {
        if (!__instance.isDone && __instance.target != null && __instance.target.isPlayer
                && AbstractSummonPower.isSummonPower(___powerToApply)) {
            AbstractPower restriction = __instance.target.getPower(GraftedDragonPower.POWER_ID);
            if (restriction != null && restriction.amount > 0) {
                // Reject before initial application, stacking, and summon-trigger hooks.
                restriction.flash();
                __instance.isDone = true;
                return SpireReturn.Return(null);
            }
        }
        return SpireReturn.Continue();
    }
}
