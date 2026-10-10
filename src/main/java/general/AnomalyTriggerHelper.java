package general;

import actions.TriggerAnomalyTicksAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.CalamitySpraymistPower;

public final class AnomalyTriggerHelper {
    private AnomalyTriggerHelper() {}

    public static int extraRounds(AbstractCreature target) {
        if (!CombatState.isInCombat() || target == null || AbstractDungeon.player == null) {
            return 0;
        }
        AbstractPower power = AbstractDungeon.player.getPower(CalamitySpraymistPower.POWER_ID);
        return power == null ? 0 : Math.max(0, power.amount);
    }

    public static void queueTrigger(AbstractPower power, AbstractCreature source, int ticksPerRound) {
        long ticks = (1L + extraRounds(power.owner)) * ticksPerRound;
        AbstractDungeon.actionManager.addToBottom(
                new TriggerAnomalyTicksAction(power.owner, source, power.ID, ticks));
    }

    public static int previewDamage(AbstractCreature target, String powerId, int ticksPerRound) {
        AbstractPower power = target.getPower(powerId);
        if (power == null || power.amount <= 0) {
            return 0;
        }
        long ticks = Math.min((long)power.amount, (1L + extraRounds(target)) * ticksPerRound);
        if (target.hasPower("Intangible") || target.hasPower("IntangiblePlayer")) {
            return (int)ticks;
        }
        long damage = ticks * (2L * power.amount - ticks + 1L) / 2L;
        return (int)Math.min(Integer.MAX_VALUE, damage);
    }
}
