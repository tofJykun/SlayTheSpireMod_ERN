package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class NextTurnIntelligencePower extends AbstractPower {
    public static final String POWER_ID = "NextTurnIntelligencePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public NextTurnIntelligencePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        loadRegion("skillBurn");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot((AbstractGameAction)new ApplyPowerAction(this.owner, this.owner,
                new IntelligencePower(this.owner, this.amount), this.amount));
        addToBot((AbstractGameAction)new ApplyPowerAction(this.owner, this.owner,
                new FadingIntelligencePower(this.owner, this.amount), this.amount));
        addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
