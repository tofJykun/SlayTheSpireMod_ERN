package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.DeadlyDancePower;

@SpirePatch(clz = ApplyPowerAction.class, method = "update")
public class DeadlyDancePatch {
    @SpireInsertPatch(locator = PlayerDebuffStatsPatch.SuccessLocator.class)
    public static void insert(ApplyPowerAction __instance, AbstractPower ___powerToApply) {
        if (AbstractDungeon.player != null) {
            AbstractPower power = AbstractDungeon.player.getPower(DeadlyDancePower.POWER_ID);
            if (power instanceof DeadlyDancePower) {
                ((DeadlyDancePower)power).onDebuffApplied(___powerToApply, __instance.target,
                        __instance.source, __instance.amount);
            }
        }
    }
}
