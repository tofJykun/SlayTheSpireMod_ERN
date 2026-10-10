package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.PlayerDebuffStats;
import powers.HolyGroundPower;

@SpirePatch(clz = ApplyPowerAction.class, method = "update")
public class HolyGroundPatch {
    @SpireInsertPatch(locator = PlayerDebuffStatsPatch.SuccessLocator.class)
    public static void insert(ApplyPowerAction __instance, AbstractPower ___powerToApply) {
        if (PlayerDebuffStats.isPlayerDebuffApplication(__instance.target, ___powerToApply, __instance.amount)) {
            AbstractPower power = AbstractDungeon.player.getPower(HolyGroundPower.POWER_ID);
            if (power instanceof HolyGroundPower) {
                ((HolyGroundPower)power).onDebuffApplied();
            }
        }
    }
}
