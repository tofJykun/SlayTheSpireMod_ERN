package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import powers.GreyHealthPower;

public class GreyHealthSettleAction extends AbstractGameAction {
    public GreyHealthSettleAction(AbstractCreature target) {
        this.target = target;
        this.source = target;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        if (this.target == null || this.target.isDeadOrEscaped()) {
            this.isDone = true;
            return;
        }

        if (this.target.hasPower(GreyHealthPower.POWER_ID)) {
            int greyHealth = this.target.getPower(GreyHealthPower.POWER_ID).amount;
            if (greyHealth > 0) {
                addToBot(new GreyHealthLoseHpAction(this.target, this.target, greyHealth));
            }
            addToBot(new RemoveSpecificPowerAction(this.target, this.target, GreyHealthPower.POWER_ID));
        }
        this.isDone = true;
    }
}
