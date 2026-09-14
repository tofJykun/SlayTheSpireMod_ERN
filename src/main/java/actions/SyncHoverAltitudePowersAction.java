package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.DeepSpacePower;
import powers.HoverPower;
import powers.LoftyPower;

public class SyncHoverAltitudePowersAction extends AbstractGameAction {
    public SyncHoverAltitudePowersAction(AbstractCreature owner) {
        this.target = owner;
        this.source = owner;
        this.actionType = ActionType.POWER;
    }

    @Override
    public void update() {
        if (this.target == null || this.target.isDeadOrEscaped()) {
            this.isDone = true;
            return;
        }

        AbstractPower hover = this.target.getPower(HoverPower.POWER_ID);
        int hoverAmount = hover == null ? 0 : hover.amount;
        syncPower(DeepSpacePower.POWER_ID, hoverAmount >= HoverPower.DEEP_SPACE_THRESHOLD,
                new DeepSpacePower(this.target));
        syncPower(LoftyPower.POWER_ID,
                hoverAmount >= HoverPower.LOFTY_THRESHOLD && hoverAmount < HoverPower.DEEP_SPACE_THRESHOLD,
                new LoftyPower(this.target));
        this.isDone = true;
    }

    private void syncPower(String powerId, boolean shouldHave, AbstractPower powerToApply) {
        if (shouldHave) {
            if (!this.target.hasPower(powerId)) {
                addToTop(new ApplyPowerAction(this.target, this.source, powerToApply));
            }
        } else if (this.target.hasPower(powerId)) {
            addToTop(new RemoveSpecificPowerAction(this.target, this.source, powerId));
        }
    }
}
