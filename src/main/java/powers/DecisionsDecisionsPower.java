package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DecisionsDecisionsPower extends AbstractPower {
    public static final String POWER_ID = "DecisionsDecisionsPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private static final int CARD_THRESHOLD = 5;
    private int drawDiscardAmount;

    public DecisionsDecisionsPower(AbstractCreature owner, int drawDiscardAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = CARD_THRESHOLD;
        this.drawDiscardAmount = drawDiscardAmount;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.drawDiscardAmount += stackAmount;
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.amount <= 1) {
            flash();
            addToBot((AbstractGameAction)new DrawCardAction(this.owner, this.drawDiscardAmount));
            addToBot((AbstractGameAction)new DiscardAction(this.owner, this.owner, this.drawDiscardAmount, false));
            this.amount = CARD_THRESHOLD;
        } else {
            this.amount--;
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (this.drawDiscardAmount == 1) {
            this.description = DESCRIPTIONS[0] + CARD_THRESHOLD + DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[2] + CARD_THRESHOLD + DESCRIPTIONS[3]
                    + this.drawDiscardAmount + DESCRIPTIONS[4]
                    + this.drawDiscardAmount + DESCRIPTIONS[5];
        }
    }
}
