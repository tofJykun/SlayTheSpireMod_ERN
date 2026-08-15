package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PowerOfErdtreePower extends AbstractPower {
    public static final String POWER_ID = "PowerOfErdtreePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public PowerOfErdtreePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.priority = -100;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public int onLoseHp(int damageAmount) {
        if (damageAmount <= 0) {
            return damageAmount;
        }
        flash();
        this.owner.lastDamageTaken = 0;
        return 0;
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount = -1;
        flash();
    }

    @Override
    public void atStartOfTurn() {
        addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
