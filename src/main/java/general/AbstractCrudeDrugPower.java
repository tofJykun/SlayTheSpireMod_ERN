package general;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class AbstractCrudeDrugPower extends AbstractPower {
    protected AbstractCrudeDrugPower(AbstractCreature owner) {
        this.owner = owner;
        this.amount = 1;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        this.canGoNegative = false;
    }
}
