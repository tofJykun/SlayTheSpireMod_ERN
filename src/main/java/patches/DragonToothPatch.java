package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.DragonToothPower;
import powers.MadnessPower;

@SpirePatch(clz = ApplyPowerAction.class, method = "update")
public class DragonToothPatch {
    @SpirePrefixPatch
    public static SpireReturn<Void> prefix(ApplyPowerAction __instance, AbstractPower ___powerToApply) {
        if (!__instance.isDone && __instance.target != null && __instance.target.isPlayer
                && ___powerToApply != null && MadnessPower.POWER_ID.equals(___powerToApply.ID)
                && __instance.amount > 0) {
            AbstractPower immunity = __instance.target.getPower(DragonToothPower.POWER_ID);
            if (immunity != null) {
                // Cancel before Artifact, stacking, and successful-application hooks.
                immunity.flash();
                __instance.isDone = true;
                return SpireReturn.Return(null);
            }
        }
        return SpireReturn.Continue();
    }
}
