package powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class InsightPower extends AbstractPower {
    public static final String POWER_ID = "InsightPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private int usedThisTurn = 0;

    public InsightPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        updateDescription();
        PowerIconHelper.load(this, POWER_ID);
    }

    @Override
    public void atStartOfTurn() {
        this.usedThisTurn = 0;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateDescription();
    }

    public boolean canChooseRandomPlay() {
        return this.usedThisTurn < this.amount;
    }

    public void consumeRandomPlayChoice() {
        this.usedThisTurn++;
        flash();
        updateDescription();
    }

    public String getPrompt() {
        return DESCRIPTIONS[2];
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}

