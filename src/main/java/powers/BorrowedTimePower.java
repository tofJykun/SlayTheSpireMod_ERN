package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BorrowedTimePower extends AbstractPower {
    public static final String POWER_ID = "BorrowedTimePower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    private final Map<UUID, Integer> originalCosts = new HashMap<>();
    private final Map<UUID, Boolean> originalModifiedFlags = new HashMap<>();

    public BorrowedTimePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        updateCardsInHand();
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        updateCardsInHand();
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        updateCard(card);
    }

    @Override
    public void onDrawOrDiscard() {
        updateCardsInHand();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateCardsInHand();
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
    }

    @Override
    public void onRemove() {
        restoreCards();
    }

    private void updateCardsInHand() {
        if (AbstractDungeon.player == null) {
            return;
        }
        updateGroup(AbstractDungeon.player.hand);
    }

    private void updateGroup(CardGroup group) {
        for (AbstractCard card : group.group) {
            updateCard(card);
        }
    }

    private void updateCard(AbstractCard card) {
        if (card == null || card.cost < 0 || card.costForTurn < 0) {
            return;
        }
        if (!this.originalCosts.containsKey(card.uuid)) {
            this.originalCosts.put(card.uuid, card.costForTurn);
            this.originalModifiedFlags.put(card.uuid, card.isCostModifiedForTurn);
        }
        int increasedCost = this.originalCosts.get(card.uuid) + this.amount;
        if (card.costForTurn < increasedCost) {
            card.setCostForTurn(increasedCost);
        }
    }

    private void restoreCards() {
        if (AbstractDungeon.player == null) {
            return;
        }
        restoreGroup(AbstractDungeon.player.hand);
        restoreGroup(AbstractDungeon.player.drawPile);
        restoreGroup(AbstractDungeon.player.discardPile);
        restoreGroup(AbstractDungeon.player.exhaustPile);
    }

    private void restoreGroup(CardGroup group) {
        for (AbstractCard card : group.group) {
            if (this.originalCosts.containsKey(card.uuid)) {
                card.costForTurn = this.originalCosts.get(card.uuid);
                card.isCostModifiedForTurn = this.originalModifiedFlags.get(card.uuid);
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
