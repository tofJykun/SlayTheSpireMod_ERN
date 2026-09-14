package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class LoseStrengthAtEndOfTurnPower extends AbstractPower {
    public static final String POWER_ID = "LoseStrengthAtEndOfTurnPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public LoseStrengthAtEndOfTurnPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        flash();
        loseStrength();
        addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    private void loseStrength() {
        if (this.owner.hasPower(StrengthPower.POWER_ID)) {
            AbstractPower strength = this.owner.getPower(StrengthPower.POWER_ID);
            strength.stackPower(-this.amount);
            strength.updateDescription();
            if (strength.amount == 0) {
                addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, strength));
            }
        } else {
            this.owner.addPower(new StrengthPower(this.owner, -this.amount));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
