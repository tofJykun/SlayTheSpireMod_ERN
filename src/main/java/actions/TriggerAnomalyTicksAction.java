package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CombatState;
import powers.BloodburnPower;
import powers.ScarletRotPower;

/** Reads each tick after the preceding damage and stack reduction have resolved. */
public class TriggerAnomalyTicksAction extends AbstractGameAction {
    private final String powerId;
    private final long ticks;

    public TriggerAnomalyTicksAction(AbstractCreature target, AbstractCreature source, String powerId, long ticks) {
        this.target = target;
        this.source = source;
        this.powerId = powerId;
        this.ticks = ticks;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        this.isDone = true;
        if (this.ticks <= 0 || !CombatState.isInCombat() || this.target == null
                || this.target.currentHealth <= 0 || this.target.isDeadOrEscaped()) {
            return;
        }
        AbstractPower power = this.target.getPower(this.powerId);
        if (power == null || power.amount <= 0) {
            return;
        }
        if (this.ticks > 1) {
            addToTop(new TriggerAnomalyTicksAction(this.target, this.source, this.powerId, this.ticks - 1));
        }
        if ("Poison".equals(this.powerId)) {
            addToTop(new PoisonLoseHpAction(this.target, this.source, power.amount, AttackEffect.POISON));
        } else if (ScarletRotPower.POWER_ID.equals(this.powerId)) {
            addToTop(new ScarletRotLoseHpAction(this.target, this.source, AttackEffect.POISON));
        } else if (BloodburnPower.POWER_ID.equals(this.powerId)) {
            addToTop(new BloodburnLoseHpAction(this.target, this.source, AttackEffect.POISON));
        }
    }
}
