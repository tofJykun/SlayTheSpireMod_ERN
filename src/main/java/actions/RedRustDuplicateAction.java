package actions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

// Only this application skips Red Rust; other power-application listeners still run.
public class RedRustDuplicateAction extends ApplyPowerAction {
    public RedRustDuplicateAction(AbstractCreature target, AbstractCreature source,
                                  AbstractPower power, int amount) {
        super(target, source, power, amount);
    }
}
