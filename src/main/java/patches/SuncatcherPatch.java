package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import general.PlayerDebuffStats;
import relics.Suncatcher;

@SpirePatch(clz = ApplyPowerAction.class, method = "update")
public class SuncatcherPatch {
    @SpireInsertPatch(locator = PlayerDebuffStatsPatch.SuccessLocator.class)
    public static void insert(ApplyPowerAction __instance, AbstractPower ___powerToApply) {
        if (PlayerDebuffStats.isPlayerDebuffApplication(__instance.target, ___powerToApply, __instance.amount)) {
            AbstractRelic relic = AbstractDungeon.player.getRelic(Suncatcher.ID);
            if (relic instanceof Suncatcher) {
                ((Suncatcher)relic).onDebuffApplied();
            }
        }
    }
}
