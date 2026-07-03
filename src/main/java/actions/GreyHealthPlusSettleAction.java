package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import powers.GreyHealthPlusPower;

public class GreyHealthPlusSettleAction extends AbstractGameAction {
    public GreyHealthPlusSettleAction(AbstractCreature target) {
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

        if (this.target.hasPower(GreyHealthPlusPower.POWER_ID)) {
            int greyHealth = this.target.getPower(GreyHealthPlusPower.POWER_ID).amount;
            if (greyHealth > 0) {
                addToBot(new GreyHealthLoseHpAction(this.target, this.target, greyHealth));
            }
            addToBot(new RemoveSpecificPowerAction(this.target, this.target, GreyHealthPlusPower.POWER_ID));
        }
        this.isDone = true;
    }
}
