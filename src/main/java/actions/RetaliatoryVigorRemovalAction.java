package actions;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import powers.RetaliatoryPower;

/** Only Vigor removals caused by playing an attack use this action. */
public class RetaliatoryVigorRemovalAction extends RemoveSpecificPowerAction {
    private final AbstractCreature owner;
    private final boolean refundByCard;
    private boolean checked;

    public RetaliatoryVigorRemovalAction(AbstractCreature owner) {
        this(owner, false);
    }

    public RetaliatoryVigorRemovalAction(AbstractCreature owner, boolean refundByCard) {
        super(owner, owner, VigorPower.POWER_ID);
        this.owner = owner;
        this.refundByCard = refundByCard;
    }

    @Override
    public void update() {
        if (checked) {
            super.update();
            return;
        }
        checked = true;
        AbstractPower vigor = owner.getPower(VigorPower.POWER_ID);
        int consumed = vigor == null ? 0 : vigor.amount;
        super.update();
        // Native removal happens on the first tick. Refund only an actual removal.
        if (vigor != null && owner.getPower(VigorPower.POWER_ID) != vigor && !owner.isDeadOrEscaped()) {
            AbstractPower power = owner.getPower(RetaliatoryPower.POWER_ID);
            if (power instanceof RetaliatoryPower) {
                ((RetaliatoryPower)power).onVigorConsumed(consumed);
            }
            if (refundByCard && consumed > 0) {
                addToTop(new ApplyPowerAction(owner, owner, new VigorPower(owner, consumed), consumed));
            }
        }
    }
}
