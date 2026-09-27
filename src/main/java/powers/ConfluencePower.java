package powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ConfluencePower extends AbstractPower {
    public static final String POWER_ID = "ConfluencePower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private static final int MIN_COST = 2;
    private static final int CHEMICAL_X_BONUS = 2;

    public ConfluencePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 0;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        this.amount = 0;
        refreshCards();
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.amount = 0;
            refreshCards();
            updateDescription();
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!isHighCost(card)) {
            return;
        }
        this.amount++;
        flash();
        refreshCards();
        updateDescription();
    }

    private boolean isHighCost(AbstractCard card) {
        if (card == null) {
            return false;
        }
        if (card.cost == -1) {
            int xValue = Math.max(0, card.energyOnUse);
            if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic("Chemical X")) {
                xValue += CHEMICAL_X_BONUS;
            }
            return xValue >= MIN_COST;
        }
        return card.costForTurn >= MIN_COST;
    }

    private void refreshCards() {
        if (AbstractDungeon.player == null) {
            return;
        }
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card != null && "Confluence".equals(card.cardID)) {
                card.applyPowers();
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + MIN_COST + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }
}
