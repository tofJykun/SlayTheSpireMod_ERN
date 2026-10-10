package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import general.CombatState;
import relics.BadApple;

@SpirePatch(clz = ApplyPowerAction.class, method = "update")
public class BadApplePatch {
    @SpireInsertPatch(locator = PlayerDebuffStatsPatch.SuccessLocator.class)
    public static void insert(ApplyPowerAction __instance, AbstractPower ___powerToApply) {
        if (!CombatState.isInCombat() || AbstractDungeon.player == null
                || __instance.source != AbstractDungeon.player
                || !(__instance.target instanceof AbstractMonster) || ___powerToApply == null
                || ___powerToApply.type != AbstractPower.PowerType.DEBUFF || __instance.amount == 0
                || (__instance.amount < 0 && !___powerToApply.canGoNegative)) return;
        AbstractMonster target = (AbstractMonster)__instance.target;
        if (target.isDeadOrEscaped() || target.halfDead || target.currentHealth <= 0) return;
        AbstractRelic relic = AbstractDungeon.player.getRelic(BadApple.ID);
        if (relic instanceof BadApple) {
            ((BadApple)relic).onDebuffApplied();
        }
    }
}
