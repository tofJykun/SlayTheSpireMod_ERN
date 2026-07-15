package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class BloodlossReduceMaxHealthAction extends AbstractGameAction {
    public BloodlossReduceMaxHealthAction(AbstractCreature target, int amount) {
        this.target = target;
        this.amount = amount;
    }

    @Override
    public void update() {
        if (this.target == null || this.target.isDeadOrEscaped() || this.amount <= 0) {
            this.isDone = true;
            return;
        }

        if (this.target.maxHealth - this.amount <= 0) {
            this.target.maxHealth = 0;
            this.target.currentHealth = 0;
            this.target.healthBarUpdatedEvent();
            addToTop(new InstantKillAction(this.target));
        } else {
            this.target.maxHealth -= this.amount;
            if (this.target.currentHealth > this.target.maxHealth) {
                this.target.currentHealth = this.target.maxHealth;
            }
            this.target.healthBarUpdatedEvent();
        }

        this.isDone = true;
    }
}
