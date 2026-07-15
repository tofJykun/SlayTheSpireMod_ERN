package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PoiseBreakPower extends AbstractPower {
    public static final String POWER_ID = "PoiseBreakPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int THRESHOLD = 10;

    public PoiseBreakPower(AbstractCreature owner, int amount) {
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
    public void onInitialApplication() {
        checkThreshold();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        checkThreshold();
    }

    private void checkThreshold() {
        while (this.amount >= THRESHOLD && !this.owner.isDeadOrEscaped()) {
            this.amount -= THRESHOLD;
            triggerPoiseBreak();
        }

        if (this.amount <= 0) {
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            updateDescription();
        }
    }

    private void triggerPoiseBreak() {
        flash();
        if (this.owner instanceof AbstractMonster) {
            AbstractMonster monster = (AbstractMonster)this.owner;
            addToBot((AbstractGameAction)new ApplyPowerAction(monster, monster, new StunPower(monster, 1), 1));
        } else if (this.owner.isPlayer) {
            addToBot((AbstractGameAction)new PressEndTurnButtonAction());
        }
    }

    @Override
    public void updateDescription() {
        if (this.owner != null && this.owner.isPlayer) {
            this.description = DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[0];
        }
    }
}

