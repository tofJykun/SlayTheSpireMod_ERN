package general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.GreyHealthPlusPower;
import powers.GreyHealthPower;

import java.util.ArrayList;

public final class GreyHealthAttackReduction {
    private final AbstractPower power;
    private final ArrayList<ReductionAction> pending = new ArrayList<>();

    public GreyHealthAttackReduction(AbstractPower power) {
        this.power = power;
    }

    public void queue(int amount) {
        ReductionAction action = new ReductionAction(amount);
        this.pending.add(action);
        AbstractDungeon.actionManager.addToBottom(action);
    }

    public void settlePending() {
        for (ReductionAction action : new ArrayList<>(this.pending)) {
            action.resolve(false);
        }
    }

    public static void settleBeforeVictory(AbstractCreature owner) {
        if (owner == null) {
            return;
        }
        // Both types must be reduced before Blue Tearstone Ring evaluates either one.
        for (AbstractPower power : owner.powers) {
            if (power instanceof GreyHealthPower) {
                ((GreyHealthPower)power).settlePendingAttackReductions();
            } else if (power instanceof GreyHealthPlusPower) {
                ((GreyHealthPlusPower)power).settlePendingAttackReductions();
            }
        }
    }

    private final class ReductionAction extends AbstractGameAction {
        private boolean resolved;

        private ReductionAction(int amount) {
            setValues(power.owner, power.owner, amount);
            this.actionType = ActionType.REDUCE_POWER;
        }

        @Override
        public void update() {
            resolve(true);
            this.isDone = true;
        }

        private void resolve(boolean removeEmptyPower) {
            if (this.resolved) {
                return;
            }
            this.resolved = true;
            pending.remove(this);
            if (power.owner == null || power.owner.getPower(power.ID) != power || power.amount <= 0) {
                return;
            }
            power.reducePower(Math.min(power.amount, this.amount));
            power.updateDescription();
            AbstractDungeon.onModifyPower();
            // Victory iterates owner.powers; leave zero-stack entries for combat cleanup.
            if (removeEmptyPower && power.amount == 0) {
                addToTop(new RemoveSpecificPowerAction(power.owner, power.owner, power));
            }
        }
    }
}
