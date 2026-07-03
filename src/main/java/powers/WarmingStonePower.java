package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class WarmingStonePower extends AbstractPower {
    public static final String POWER_ID = "WarmingStonePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int TRIGGER_TURNS = 3;

    private int turnsLeft;

    public WarmingStonePower(AbstractCreature owner, int vigorAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = vigorAmount;
        this.turnsLeft = TRIGGER_TURNS;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        loadRegion("energized_blue");
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        this.turnsLeft = Math.max(this.turnsLeft, TRIGGER_TURNS);
        updateDescription();
    }

    @Override
    public void onEnergyRecharge() {
        if (this.turnsLeft <= 0 || this.amount <= 0) {
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
            return;
        }

        flash();
        addToBot((AbstractGameAction)new ApplyPowerAction(this.owner, this.owner,
                new VigorPower(this.owner, this.amount), this.amount));
        this.turnsLeft--;
        if (this.turnsLeft <= 0) {
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.turnsLeft + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }
}
