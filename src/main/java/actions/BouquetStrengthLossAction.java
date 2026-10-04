package actions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class BouquetStrengthLossAction extends ApplyPowerAction {
    private final AbstractCreature recipient;
    private final AbstractCreature applier;
    private final int loss;
    private boolean recorded;

    public BouquetStrengthLossAction(AbstractCreature target, AbstractCreature source, int amount) {
        super(target, source, new StrengthPower(target, -amount), -amount);
        recipient = target;
        applier = source;
        loss = amount;
    }

    @Override
    public void update() {
        if (recorded) {
            super.update();
            return;
        }
        recorded = true;
        int before = strength();
        super.update();
        // Artifact and stat caps must not create a refund for Strength that was never lost.
        int lost = Math.min(loss, Math.max(0, before - strength()));
        if (lost > 0) {
            addToTop(new ApplyPowerAction(recipient, applier, new GainStrengthPower(recipient, lost), lost));
        }
    }

    private int strength() {
        AbstractPower power = recipient.getPower(StrengthPower.POWER_ID);
        return power == null ? 0 : power.amount;
    }
}
