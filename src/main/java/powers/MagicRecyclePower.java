package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MagicRecyclePower extends AbstractPower {
    public static final String POWER_ID = "MagicRecyclePower";
    private static final int DEFAULT_DURATION = 3;
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int turnsRemaining;
    private boolean activeThisTurn;
    private boolean triggeredThisTurn;
    private boolean refreshAfterCurrentTurn;

    public MagicRecyclePower(AbstractCreature owner, int duration, int healAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = healAmount;
        this.turnsRemaining = duration;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        this.turnsRemaining = Math.max(this.turnsRemaining, DEFAULT_DURATION);
        this.refreshAfterCurrentTurn = this.activeThisTurn;
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        this.activeThisTurn = true;
        this.triggeredThisTurn = false;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.activeThisTurn && !this.triggeredThisTurn
                && card.type == AbstractCard.CardType.POWER && this.amount > 0) {
            this.triggeredThisTurn = true;
            flash();
            addToBot((AbstractGameAction)new HealAction(this.owner, this.owner, this.amount));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer || !this.activeThisTurn) {
            return;
        }

        this.activeThisTurn = false;
        if (this.refreshAfterCurrentTurn) {
            this.turnsRemaining = DEFAULT_DURATION;
            this.refreshAfterCurrentTurn = false;
        } else {
            --this.turnsRemaining;
        }

        if (this.turnsRemaining <= 0) {
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.turnsRemaining + DESCRIPTIONS[1]
                + this.amount + DESCRIPTIONS[2];
    }
}
