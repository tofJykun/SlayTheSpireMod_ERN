package powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BudgetTravelPower extends AbstractPower {
    public static final String POWER_ID = "BudgetTravelPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public BudgetTravelPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot(new LoseEnergyAction(this.amount));
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    }

    @Override
    public void updateDescription() {
        this.description = STRINGS.DESCRIPTIONS[0] + this.amount + STRINGS.DESCRIPTIONS[1];
    }
}
