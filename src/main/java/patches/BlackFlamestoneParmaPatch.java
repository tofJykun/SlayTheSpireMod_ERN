package patches;

import cards.executor.BlackFlamestoneParma;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import general.CombatState;
import general.SmithingBody;
import powers.BloodburnPower;
import powers.ScarletRotPower;

@SpirePatch(clz = ApplyPowerAction.class, method = "update")
public class BlackFlamestoneParmaPatch {
    @SpireInsertPatch(locator = PlayerDebuffStatsPatch.SuccessLocator.class)
    public static void insert(ApplyPowerAction __instance, AbstractPower ___powerToApply) {
        if (!CombatState.isInCombat() || AbstractDungeon.player == null
                || AbstractDungeon.actionManager == null || ___powerToApply == null
                || __instance.source != AbstractDungeon.player || __instance.amount <= 0
                || !(__instance.target instanceof AbstractMonster)) {
            return;
        }
        AbstractMonster target = (AbstractMonster)__instance.target;
        if (target.isDeadOrEscaped() || target.halfDead || target.currentHealth <= 0) {
            return;
        }
        String id = ___powerToApply.ID;
        if (!PoisonPower.POWER_ID.equals(id) && !ScarletRotPower.POWER_ID.equals(id)
                && !BloodburnPower.POWER_ID.equals(id)) {
            return;
        }
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (SmithingBody.behavior(card) instanceof BlackFlamestoneParma) {
                // Return the physical card, including a forged creation, never its internal body.
                AbstractDungeon.actionManager.addToBottom(new DiscardToHandAction(card));
            }
        }
    }
}
