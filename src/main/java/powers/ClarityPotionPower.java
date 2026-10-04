package powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ClarityPotionPower extends AbstractPower {
    public static final String POWER_ID = "ClarityPotionPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private int followingTurnAmount;

    public ClarityPotionPower(AbstractCreature owner, int amount) {
        ID = POWER_ID;
        name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        followingTurnAmount = amount;
        type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        amount += stackAmount;
        followingTurnAmount += stackAmount;
        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        if (amount <= 0) return;
        flash();
        addToBot(new DrawCardAction(owner, amount));
        addToBot(new GainEnergyAction(amount));
        // Shift the two future-turn totals; new uses add to both remaining slots.
        amount = followingTurnAmount;
        followingTurnAmount = 0;
        updateDescription();
        if (amount == 0) addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], amount, amount);
        if (followingTurnAmount > 0) {
            description += String.format(STRINGS.DESCRIPTIONS[1], followingTurnAmount, followingTurnAmount);
        }
    }
}
