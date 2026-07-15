package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
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

public class BedOfMagicCostReductionPower extends AbstractPower {
    public static final String POWER_ID = "BedOfMagicCostReductionPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final AbstractCard.CardType targetType;
    private final Map<UUID, Integer> originalCosts = new HashMap<>();
    private final Map<UUID, Boolean> originalModifiedFlags = new HashMap<>();

    public BedOfMagicCostReductionPower(AbstractCreature owner, AbstractCard.CardType targetType, int amount) {
        this.name = NAME;
        this.ID = POWER_ID + "_" + targetType.name();
        this.owner = owner;
        this.targetType = targetType;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        reduceCombatCards();
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        reduceHandCards();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        reduceCombatCards();
        updateDescription();
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        reduceCard(card);
    }

    @Override
    public void onDrawOrDiscard() {
        reduceHandCards();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == this.targetType && this.amount > 0) {
            this.amount--;
            updateDescription();
            if (this.amount == 0) {
                addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
    }

    @Override
    public void onRemove() {
        restoreCombatCards();
    }

    @Override
    public void updateDescription() {
        String typeText = this.targetType == AbstractCard.CardType.ATTACK ? DESCRIPTIONS[1] : DESCRIPTIONS[2];
        this.description = DESCRIPTIONS[0] + this.amount + typeText + DESCRIPTIONS[3];
    }

    private void reduceCombatCards() {
        if (AbstractDungeon.player == null || this.amount <= 0) {
            return;
        }
        reduceHandCards();
    }

    private void reduceHandCards() {
        if (AbstractDungeon.player == null || this.amount <= 0) {
            return;
        }
        reduceGroup(AbstractDungeon.player.hand);
    }

    private void reduceGroup(CardGroup group) {
        for (AbstractCard card : group.group) {
            reduceCard(card);
        }
    }

    private void reduceCard(AbstractCard card) {
        if (card.type != this.targetType || card.cost < 0 || card.costForTurn <= 0 || this.amount <= 0) {
            return;
        }
        if (!this.originalCosts.containsKey(card.uuid)) {
            this.originalCosts.put(card.uuid, card.costForTurn);
            this.originalModifiedFlags.put(card.uuid, card.isCostModifiedForTurn);
        }
        int reducedCost = this.originalCosts.get(card.uuid) - 1;
        if (card.costForTurn > reducedCost) {
            card.setCostForTurn(reducedCost);
        }
    }

    private void restoreCombatCards() {
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
}

