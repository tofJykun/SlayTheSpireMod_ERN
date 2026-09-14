package cards.tempcards;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import basemod.helpers.TooltipInfo;
import cards.AbstractScheduledCard;
import cards.raider.CourtesyEnough;
import cards.raider.Kick;
import actions.RandomPlayHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import general.SmithingHelper;
import patches.InsuranceField;
import patches.ReplayField;
import patches.ScheduledField;
import powers.MasterworkPower;
import relics.LargeEmber;
import patches.LargeEmberStrikePatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CraftmanCreation extends AbstractScheduledCard {
    public static final String ID = "CraftmanCreation";
    private static final String IMG_PATH = "img/cards/tempcards/CraftmanCreation.png";

    private AbstractCard infusionMaterial;
    private AbstractCard bodyCard;
    private AbstractCard reinforcementMaterial;
    private AbstractCard pendingInfusionMaterial;
    private AbstractCard pendingReinforcementMaterial;
    private boolean needsPostUseRefresh;
    private AbstractCard scheduledBodyPreview;
    private AbstractCard smithingBodyPreview;
    private int lastPartsCost;
    private boolean largeEmberActive;

    public CraftmanCreation() {
        this(null, null, null);
    }

    public CraftmanCreation(AbstractCard infusionMaterial, AbstractCard bodyCard, AbstractCard reinforcementMaterial) {
        super(ID, getCardStrings().NAME, IMG_PATH,
                SmithingHelper.defaultCraftmanCost(bodyCard, infusionMaterial, reinforcementMaterial),
                getCardStrings().DESCRIPTION,
                bodyCard == null ? CardType.SKILL : bodyCard.type,
                CardColor.COLORLESS, CardRarity.SPECIAL,
                SmithingHelper.combinedTarget(infusionMaterial, bodyCard, reinforcementMaterial), 0);
        this.infusionMaterial = copyCard(infusionMaterial);
        this.bodyCard = copyCard(bodyCard);
        this.reinforcementMaterial = copyCard(reinforcementMaterial);
        InsuranceField.inherit(this, this.bodyCard);
        syncFromParts();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        return ScheduledField.isScheduled(this) ? super.getCustomTooltips() : Collections.emptyList();
    }

    @Override
    public void onScheduledCountChanged(int current, int base) {
        if (this.scheduledBodyPreview != null) {
            ScheduledField.setScheduled(this.scheduledBodyPreview, current);
        }
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        if (!(effectiveBodyCard() instanceof Kick)
                || AbstractDungeon.player == null
                || AbstractDungeon.player.hand == null
                || AbstractDungeon.actionManager == null
                || AbstractDungeon.actionManager.cardsPlayedThisTurn.size() >= 999
                || AbstractDungeon.getMonsters() == null
                || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            return;
        }
        // As with Kick, native draw/use owns pile movement; no limbo reference is needed.
        RandomPlayHelper.prepareRandomPlayedCard(this);
        boolean randomTarget = this.target == CardTarget.ENEMY
                || this.target == CardTarget.SELF_AND_ENEMY;
        if (randomTarget) {
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(this, true,
                    EnergyPanel.getCurrentEnergy(), true, true), true);
        } else {
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(this, null,
                    EnergyPanel.getCurrentEnergy(), true, true), true);
        }
    }

    @Override
    public void triggerOnExhaust() {
        super.triggerOnExhaust();
        AbstractCard effectiveBody = effectiveBodyCard();
        if (effectiveBody instanceof CourtesyEnough) {
            effectiveBody.triggerOnExhaust();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        SmithingHelper.playComponent(
                SmithingHelper.effectiveInfusionMaterial(this.infusionMaterial, this.reinforcementMaterial), p, m, this);
        SmithingHelper.playComponent(effectiveBodyCard(), p, m, this);
        SmithingHelper.playComponent(effectiveReinforcementMaterial(), p, m, this);
        if (SmithingHelper.randomizesInfusionMaterialAfterUse(this.reinforcementMaterial)) {
            this.pendingInfusionMaterial = SmithingHelper.randomInfusionMaterial();
            this.needsPostUseRefresh = true;
        }
    }

    public void refreshAfterUseAction() {
        boolean reforged = this.pendingReinforcementMaterial != null;
        boolean refresh = this.needsPostUseRefresh || reforged;
        if (this.needsPostUseRefresh) {
            this.needsPostUseRefresh = false;
            this.infusionMaterial = copyCard(this.pendingInfusionMaterial);
            this.pendingInfusionMaterial = null;
        }
        if (reforged) {
            this.reinforcementMaterial = copyCard(this.pendingReinforcementMaterial);
            this.pendingReinforcementMaterial = null;
        }
        if (refresh) {
            syncFromParts();
        }
        if (reforged) {
            MasterworkPower.onSmithing(AbstractDungeon.player);
        }
    }

    public void reforgeAfterUse(AbstractCard reinforcement) {
        this.pendingReinforcementMaterial = reinforcement;
    }

    public void refreshFromParts() {
        syncFromParts();
    }

    public boolean hasBodyTag(CardTags tag) {
        return this.bodyCard != null && this.bodyCard.hasTag(tag);
    }

    public void refreshLargeEmber() {
        boolean active = LargeEmber.isActive();
        if (this.largeEmberActive == active) {
            return;
        }
        int partsCost = SmithingHelper.defaultCraftmanCost(
                effectiveBodyCard(), this.infusionMaterial, this.reinforcementMaterial);
        // Change only the relic's contribution, not unrelated per-turn cost adjustments.
        if (this.cost >= 0 && partsCost >= 0 && this.lastPartsCost >= 0) {
            updateCost(partsCost - this.lastPartsCost);
        }
        this.lastPartsCost = partsCost;
        this.largeEmberActive = active;
        updateDynamicDescription();
    }

    @Override
    public void applyPowers() {
        refreshLargeEmber();
        super.applyPowers();
        refreshBodyPreview();
    }

    @Override
    public void calculateCardDamage(AbstractMonster monster) {
        refreshLargeEmber();
        super.calculateCardDamage(monster);
        refreshBodyPreview();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        CraftmanCreation copy = (CraftmanCreation)super.makeStatEquivalentCopy();
        copy.lastPartsCost = this.lastPartsCost;
        copy.largeEmberActive = this.largeEmberActive;
        copy.refreshLargeEmber();
        return copy;
    }

    @Override
    public AbstractCard makeCopy() {
        return new CraftmanCreation(this.infusionMaterial, this.bodyCard, this.reinforcementMaterial);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            syncFromParts();
        }
    }

    public AbstractCard getInfusionMaterialCopy() {
        return copyCard(this.infusionMaterial);
    }

    public AbstractCard getBodyCardCopy() {
        return copyCard(this.bodyCard);
    }

    public AbstractCard getReinforcementMaterialCopy() {
        return copyCard(this.reinforcementMaterial);
    }

    private void syncFromParts() {
        AbstractCard effectiveInfusion = effectiveInfusionMaterial();
        AbstractCard effectiveBody = effectiveBodyCard();
        AbstractCard effectiveReinforcement = effectiveReinforcementMaterial();
        this.cost = SmithingHelper.defaultCraftmanCost(effectiveBody, this.infusionMaterial, this.reinforcementMaterial);
        this.costForTurn = this.cost;
        this.lastPartsCost = this.cost;
        this.largeEmberActive = LargeEmber.isActive();
        this.type = effectiveBody == null ? CardType.SKILL : effectiveBody.type;
        this.target = SmithingHelper.combinedTarget(effectiveInfusion, effectiveBody, effectiveReinforcement);
        this.exhaust = SmithingHelper.shouldExhaust(effectiveInfusion, effectiveBody, effectiveReinforcement);
        this.selfRetain = hasSelfRetain(effectiveInfusion) || hasSelfRetain(effectiveBody)
                || hasSelfRetain(effectiveReinforcement);
        this.isEthereal = hasEthereal(effectiveInfusion) || hasEthereal(effectiveBody)
                || hasEthereal(effectiveReinforcement);
        ReplayField.setReplay(this,
                SmithingHelper.replayAmount(effectiveInfusion, effectiveBody, effectiveReinforcement));
        int bodyScheduled = ScheduledField.getBaseScheduled(effectiveBody);
        if (ScheduledField.getBaseScheduled(this) != bodyScheduled) {
            ScheduledField.setBaseScheduled(this, bodyScheduled);
        }
        updateDynamicDescription();
    }

    private void updateDynamicDescription() {
        CardStrings strings = getCardStrings();
        if (this.bodyCard == null) {
            this.rawDescription = strings.DESCRIPTION;
            setCardPreviews(null);
        } else {
            AbstractCard displayInfusion = effectiveInfusionMaterial();
            AbstractCard displayBody = effectiveBodyCard();
            AbstractCard displayReinforcement = this.reinforcementMaterial;
            StringBuilder builder = new StringBuilder();
            appendCardName(builder, displayInfusion);
            appendCardName(builder, displayBody);
            appendCardName(builder, displayReinforcement);
            this.rawDescription = builder.toString();
            setCardPreviews(displayBody, displayInfusion, displayBody, displayReinforcement);
        }
        initializeDescription();
    }

    private void setCardPreviews(AbstractCard bodyPreview, AbstractCard... previews) {
        this.cardsToPreview = null;
        this.scheduledBodyPreview = null;
        this.smithingBodyPreview = null;
        MultiCardPreview.clear(this);
        ArrayList<AbstractCard> cards = new ArrayList<>();
        for (AbstractCard preview : previews) {
            AbstractCard copy = copyCard(preview);
            if (copy != null) {
                if (preview == bodyPreview) {
                    this.smithingBodyPreview = copy;
                }
                if (preview == bodyPreview && ScheduledField.isScheduled(copy)) {
                    // Keep the displayed component in step without giving it its own hand countdown.
                    this.scheduledBodyPreview = copy;
                    ScheduledField.setScheduled(copy, ScheduledField.getScheduled(this));
                }
                cards.add(copy);
            }
        }
        if (cards.size() == 1) {
            this.cardsToPreview = cards.get(0);
        } else if (cards.size() > 1) {
            MultiCardPreview.add(this, true, cards.toArray(new AbstractCard[0]));
        }
        refreshBodyPreview();
    }

    private void refreshBodyPreview() {
        if (LargeEmber.isStrike(this.smithingBodyPreview)) {
            if (AbstractDungeon.player != null) {
                this.smithingBodyPreview.applyPowers();
            } else {
                this.smithingBodyPreview.resetAttributes();
            }
            LargeEmberStrikePatch.updateCard(this.smithingBodyPreview);
        }
    }

    private static void appendCardName(StringBuilder builder, AbstractCard card) {
        if (card == null) {
            return;
        }
        if (builder.length() > 0) {
            builder.append(", ");
        }
        builder.append(card.name);
    }

    private static AbstractCard copyCard(AbstractCard card) {
        return SmithingHelper.copyForSmithing(card);
    }

    private AbstractCard effectiveInfusionMaterial() {
        return SmithingHelper.effectiveInfusionMaterial(this.infusionMaterial, this.reinforcementMaterial);
    }

    private AbstractCard effectiveBodyCard() {
        return SmithingHelper.effectiveBodyCard(this.bodyCard, this.reinforcementMaterial, this.upgraded);
    }

    private AbstractCard effectiveReinforcementMaterial() {
        return SmithingHelper.effectiveReinforcementMaterial(this.infusionMaterial, this.reinforcementMaterial);
    }

    private static boolean hasSelfRetain(AbstractCard card) {
        return card != null && card.selfRetain;
    }

    private static boolean hasEthereal(AbstractCard card) {
        return card != null && card.isEthereal;
    }
}
