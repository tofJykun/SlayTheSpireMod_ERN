package powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DarkBloodPower extends AbstractPower {
    public static final String POWER_ID = "DarkBloodPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public DarkBloodPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        AbstractPower aberration;
        switch (AbstractDungeon.cardRandomRng.random(3)) {
            case 0:
                aberration = new FrostbitePower(this.owner, this.amount);
                break;
            case 1:
                aberration = new SleepPower(this.owner, this.amount);
                break;
            case 2:
                aberration = new MadnessPower(this.owner, this.amount);
                break;
            default:
                aberration = new BloodlossPower(this.owner, this.amount);
                break;
        }
        addToBot(new ApplyPowerAction(this.owner, this.owner, aberration, this.amount));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
