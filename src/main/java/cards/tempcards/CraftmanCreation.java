package cards.tempcards;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import basemod.helpers.TooltipInfo;
import basemod.abstracts.CustomCard;
import cards.AbstractScheduledCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import general.SmithingHelper;
import general.SmithingBody;
import general.SmithingBodyState;
import com.megacrit.cardcrawl.stances.AbstractStance;
import java.util.function.Consumer;
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
    private AbstractCard artworkBody;
    private int lastPartsCost;
    private boolean largeEmberActive;
    private SmithingBodyState bodyState;
    private int bodyUpgradeCount = -1;
    private int lastBodyCost;
    private int lastBodyTurnAdjustment;
    private boolean forwardingBody;

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
        if (bodyCard != null) this.uuid = bodyCard.uuid;
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
        if (this.bodyState != null && this.bodyState.card() != null) {
            ScheduledField.setScheduled(this.bodyState.card(), current);
        }
        if (this.scheduledBodyPreview != null) {
            ScheduledField.setScheduled(this.scheduledBodyPreview, current);
        }
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        forward(AbstractCard::triggerWhenDrawn);
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard card) {
        super.triggerOnCardPlayed(card);
        forward(body -> body.triggerOnCardPlayed(card == this ? body : card));
    }

    @Override
    public void triggerOnExhaust() {
        super.triggerOnExhaust();
        forward(AbstractCard::triggerOnExhaust);
    }

    @Override public void tookDamage() { forward(AbstractCard::tookDamage); }
    @Override public void didDiscard() { forward(AbstractCard::didDiscard); }
    @Override public void switchedStance() { forward(AbstractCard::switchedStance); }
    @Override public void onMoveToDiscard() { forward(AbstractCard::onMoveToDiscard); }
    @Override public void triggerWhenCopied() { forward(AbstractCard::triggerWhenCopied); }
    @Override public void triggerOnEndOfTurnForPlayingCard() { forward(AbstractCard::triggerOnEndOfTurnForPlayingCard); }
    @Override public void triggerOnOtherCardPlayed(AbstractCard card) { forward(body -> body.triggerOnOtherCardPlayed(card == this ? body : card)); }
    @Override public void triggerOnGainEnergy(int energy, boolean dueToCard) { forward(body -> body.triggerOnGainEnergy(energy, dueToCard)); }
    @Override public void triggerOnManualDiscard() { forward(AbstractCard::triggerOnManualDiscard); }
    @Override public void triggerOnScry() { forward(AbstractCard::triggerOnScry); }
    @Override public void triggerExhaustedCardsOnStanceChange(AbstractStance stance) { forward(body -> body.triggerExhaustedCardsOnStanceChange(stance)); }
    @Override public void triggerAtStartOfTurn() { forward(AbstractCard::triggerAtStartOfTurn); }
    @Override public void onPlayCard(AbstractCard card, AbstractMonster monster) { forward(body -> body.onPlayCard(card == this ? body : card, monster)); }
    @Override public void atTurnStart() { forward(AbstractCard::atTurnStart); }
    @Override public void atTurnStartPreDraw() { forward(AbstractCard::atTurnStartPreDraw); }
    @Override public void onChoseThisOption() { forward(AbstractCard::onChoseThisOption); }
    @Override public void onRetained() { forward(AbstractCard::onRetained); }
    @Override public void onRemoveFromMasterDeck() { forward(AbstractCard::onRemoveFromMasterDeck); }
    @Override public void resetAttributes() {
        super.resetAttributes();
        this.lastBodyTurnAdjustment = 0;
        forward(AbstractCard::resetAttributes);
    }
    @Override public void triggerOnGlowCheck() {
        forward(AbstractCard::triggerOnGlowCheck);
        if (getLiveBody() != null) this.glowColor = getLiveBody().glowColor;
    }

    @Override public void triggerOnEndOfPlayerTurn() {
        // The native callback owns Ethereal. Give it the combined flag, and call it only once.
        AbstractCard body = getLiveBody();
        if (body == null) { super.triggerOnEndOfPlayerTurn(); return; }
        boolean ethereal = body.isEthereal;
        forward(card -> {
            card.isEthereal = this.isEthereal;
            try { card.triggerOnEndOfPlayerTurn(); }
            finally { card.isEthereal = ethereal; }
        });
    }

    @Override public boolean canPlay(AbstractCard card) {
        AbstractCard body = getLiveBody();
        return body == null || body.canPlay(card);
    }

    @Override public boolean canUse(AbstractPlayer player, AbstractMonster monster) {
        if (!super.canUse(player, monster)) return false;
        AbstractCard body = getLiveBody();
        if (body == null) return true;
        boolean free = body.freeToPlayOnce;
        body.freeToPlayOnce = true;
        body.isInAutoplay = this.isInAutoplay;
        try {
            boolean allowed = body.canUse(player, monster);
            this.cantUseMessage = body.cantUseMessage;
            return allowed;
        } finally { body.freeToPlayOnce = free; }
    }

    private void forward(Consumer<AbstractCard> callback) {
        if (this.forwardingBody) return;
        AbstractCard body = getLiveBody();
        if (body == null) return;
        this.forwardingBody = true;
        try { SmithingBody.run(this, () -> callback.accept(body)); }
        finally { this.forwardingBody = false; }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int xEnergy = this.energyOnUse < 0 ? EnergyPanel.getCurrentEnergy() : this.energyOnUse;
        SmithingHelper.playComponent(
                SmithingHelper.effectiveInfusionMaterial(this.infusionMaterial, this.reinforcementMaterial), p, m, this);
        forward(body -> {
            body.energyOnUse = this.energyOnUse;
            body.freeToPlayOnce = this.freeToPlayOnce || body.cost == -1;
            body.isInAutoplay = this.isInAutoplay;
            body.purgeOnUse = this.purgeOnUse;
            ReplayField.setReplayCopy(body, ReplayField.isReplayCopy(this));
            ReplayField.setRemainingReplay(body, ReplayField.getRemainingReplay(this));
            if (m == null) body.applyPowers(); else body.calculateCardDamage(m);
            body.use(p, m);
        });
        SmithingHelper.playComponent(effectiveReinforcementMaterial(), p, m, this);
        if (this.cost == -1 && !this.freeToPlayOnce && !this.isInAutoplay) p.energy.use(xEnergy);
        if (SmithingHelper.randomizesInfusionMaterialAfterUse(this.reinforcementMaterial)) {
            this.pendingInfusionMaterial = SmithingHelper.randomInfusionMaterial();
            this.needsPostUseRefresh = true;
        }
    }

    public void refreshAfterUseAction() {
        forward(body -> {
            body.exhaustOnUseOnce = false;
            body.freeToPlayOnce = false;
            body.isInAutoplay = false;
            if (body instanceof general.PackagingCard) {
                ((general.PackagingCard)body).refreshAfterPackagingUseAction();
            }
        });
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
        updateDynamicDescription();
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
        forward(AbstractCard::applyPowers);
        refreshBodyPreview();
    }

    @Override
    public void calculateCardDamage(AbstractMonster monster) {
        refreshLargeEmber();
        super.calculateCardDamage(monster);
        forward(body -> body.calculateCardDamage(monster));
        refreshBodyPreview();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        commitBodyState();
        CraftmanCreation copy = (CraftmanCreation)super.makeStatEquivalentCopy();
        copy.uuid = java.util.UUID.randomUUID();
        if (copy.getLiveBody() != null) copy.getLiveBody().uuid = copy.uuid;
        copy.lastPartsCost = this.lastPartsCost;
        copy.largeEmberActive = this.largeEmberActive;
        copy.refreshLargeEmber();
        return copy;
    }

    @Override
    public AbstractCard makeCopy() {
        commitBodyState();
        return new CraftmanCreation(this.infusionMaterial, this.bodyCard, this.reinforcementMaterial);
    }

    @Override
    public void upgrade() {
        if (canUpgrade()) {
            upgradeName();
            syncFromParts();
        }
    }

    @Override public boolean canUpgrade() {
        return !this.upgraded || (getLiveBody() != null && getLiveBody().canUpgrade());
    }

    public AbstractCard getInfusionMaterialCopy() {
        return copyCard(this.infusionMaterial);
    }

    public AbstractCard getBodyCardCopy() {
        commitBodyState();
        return copyCard(this.bodyCard);
    }

    public AbstractCard getLiveBody() {
        AbstractCard body = effectiveBodyCard();
        if (body != null) {
            body.uuid = this.uuid;
            SmithingBody.bind(body, this);
        }
        return body;
    }

    public void commitBodyState() {
        if (this.bodyState == null || this.bodyState.card() == null) return;
        this.bodyState.commit();
        AbstractCard body = this.bodyState.card();
        this.bodyCard.uuid = this.uuid;
        if (body.cost != this.lastBodyCost) {
            int next = SmithingHelper.defaultCraftmanCost(body, this.infusionMaterial, this.reinforcementMaterial);
            if (this.cost >= 0 && next >= 0) updateCost(next - this.lastPartsCost);
            this.lastPartsCost = next;
            this.lastBodyCost = body.cost;
        }
        int adjustment = body.costForTurn - body.cost;
        if (adjustment != this.lastBodyTurnAdjustment && this.costForTurn >= 0
                && !SmithingHelper.setsCraftmanCostToZero(this.reinforcementMaterial)
                && !SmithingHelper.MURKY_HAND_SCYTHE_ID.equals(body.cardID)) {
            this.costForTurn = Math.max(0, this.costForTurn + adjustment - this.lastBodyTurnAdjustment);
            this.isCostModifiedForTurn = this.costForTurn != this.cost;
        }
        this.lastBodyTurnAdjustment = adjustment;
        AbstractCard infusion = effectiveInfusionMaterial();
        AbstractCard reinforcement = effectiveReinforcementMaterial();
        this.exhaust = SmithingHelper.shouldExhaust(infusion, body, reinforcement);
        this.selfRetain = hasSelfRetain(infusion) || body.selfRetain || hasSelfRetain(reinforcement);
        this.isEthereal = hasEthereal(infusion) || body.isEthereal || hasEthereal(reinforcement);
        this.isInnate = body.isInnate;
        this.returnToHand = body.returnToHand;
        this.shuffleBackIntoDrawPile = body.shuffleBackIntoDrawPile;
        this.exhaustOnUseOnce |= body.exhaustOnUseOnce;
        if (!ReplayField.isReplayCopy(this)) {
            ReplayField.setReplay(this, SmithingHelper.replayAmount(infusion, body, reinforcement));
        }
    }

    public CraftmanCreation copyWithBodyState(AbstractCard copiedBody) {
        CraftmanCreation copy = (CraftmanCreation)makeStatEquivalentCopy();
        copy.uuid = copiedBody.uuid;
        AbstractCard previous = copy.getLiveBody();
        int oldCost = previous.cost;
        // Keep the body's concrete Java type until its own copy override has completed.
        copy.bodyState.replace(SmithingBody.copy(copiedBody));
        SmithingBody.bind(copy.bodyState.card(), copy);
        copy.artworkBody = copy.bodyState.card();
        copy.portrait = copy.artworkBody.portrait;
        copy.jokePortrait = copy.artworkBody.jokePortrait;
        if (copiedBody.cost != oldCost) {
            copy.cost = copy.costForTurn = copiedBody.cost;
        }
        copy.updateDynamicDescription();
        return copy;
    }

    public void replaceLiveBodyState(AbstractCard body) {
        this.bodyState.replace(SmithingBody.copy(body));
        SmithingBody.bind(this.bodyState.card(), this);
        this.artworkBody = this.bodyState.card();
        this.portrait = this.artworkBody.portrait;
        this.jokePortrait = this.artworkBody.jokePortrait;
        commitBodyState();
    }

    public AbstractCard getReinforcementMaterialCopy() {
        return copyCard(this.reinforcementMaterial);
    }

    @Override
    protected Texture getPortraitImage() {
        if (this.artworkBody == null) {
            return super.getPortraitImage();
        }
        if (this.artworkBody instanceof CustomCard) {
            Texture image = CustomCard.getPortraitImage((CustomCard)this.artworkBody);
            if (image != null) {
                return image;
            }
        }
        boolean beta = Settings.PLAYTESTER_ART_MODE
                || UnlockTracker.betaCardPref.getBoolean(this.artworkBody.cardID, false);
        Texture image = ImageMaster.loadImage((beta ? "images/1024PortraitsBeta/" : "images/1024Portraits/")
                + this.artworkBody.assetUrl + ".png");
        if (image == null && !beta) {
            image = ImageMaster.loadImage("images/1024PortraitsBeta/" + this.artworkBody.assetUrl + ".png");
        }
        return image;
    }

    private void syncFromParts() {
        AbstractCard effectiveInfusion = effectiveInfusionMaterial();
        AbstractCard effectiveBody = effectiveBodyCard();
        AbstractCard effectiveReinforcement = effectiveReinforcementMaterial();
        // Generic previews keep IMG_PATH; only forged instances borrow the effective body's artwork.
        this.artworkBody = effectiveBody;
        if (effectiveBody != null) {
            this.portrait = effectiveBody.portrait;
            this.jokePortrait = effectiveBody.jokePortrait;
        }
        this.cost = SmithingHelper.defaultCraftmanCost(effectiveBody, this.infusionMaterial, this.reinforcementMaterial);
        this.costForTurn = this.cost;
        this.lastPartsCost = this.cost;
        this.lastBodyCost = effectiveBody == null ? 0 : effectiveBody.cost;
        this.lastBodyTurnAdjustment = 0;
        this.largeEmberActive = LargeEmber.isActive();
        this.type = effectiveBody == null ? CardType.SKILL : effectiveBody.type;
        this.target = SmithingHelper.combinedTarget(effectiveInfusion, effectiveBody, effectiveReinforcement);
        this.exhaust = SmithingHelper.shouldExhaust(effectiveInfusion, effectiveBody, effectiveReinforcement);
        this.selfRetain = hasSelfRetain(effectiveInfusion) || hasSelfRetain(effectiveBody)
                || hasSelfRetain(effectiveReinforcement);
        this.isEthereal = hasEthereal(effectiveInfusion) || hasEthereal(effectiveBody)
                || hasEthereal(effectiveReinforcement);
        this.isInnate = effectiveBody != null && effectiveBody.isInnate;
        this.returnToHand = effectiveBody != null && effectiveBody.returnToHand;
        this.shuffleBackIntoDrawPile = effectiveBody != null && effectiveBody.shuffleBackIntoDrawPile;
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
        if (this.bodyState != null && this.bodyState.card() != null && !this.forwardingBody) {
            AbstractCard body = this.bodyState.card();
            this.smithingBodyPreview = SmithingBody.copy(body);
            // Derived numbers belong on the preview, not in the saved component state.
            this.smithingBodyPreview.damage = body.damage;
            this.smithingBodyPreview.block = body.block;
            this.smithingBodyPreview.magicNumber = body.magicNumber;
            this.smithingBodyPreview.isDamageModified = body.isDamageModified;
            this.smithingBodyPreview.isBlockModified = body.isBlockModified;
            this.smithingBodyPreview.isMagicNumberModified = body.isMagicNumberModified;
            this.scheduledBodyPreview = ScheduledField.isScheduled(this.smithingBodyPreview) ? this.smithingBodyPreview : null;
            if (this.scheduledBodyPreview != null) ScheduledField.setScheduled(this.scheduledBodyPreview, ScheduledField.getScheduled(this));
            MultiCardPreview.clear(this);
            ArrayList<AbstractCard> previews = new ArrayList<>();
            if (this.infusionMaterial != null) previews.add(effectiveInfusionMaterial());
            previews.add(this.smithingBodyPreview);
            if (this.reinforcementMaterial != null) previews.add(copyCard(this.reinforcementMaterial));
            this.cardsToPreview = null;
            MultiCardPreview.add(this, true, previews.toArray(new AbstractCard[0]));
        }
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
        if (this.bodyCard == null) return null;
        int ownUpgrades = Math.max(this.timesUpgraded, this.upgraded ? 1 : 0);
        int upgrades = ownUpgrades + (SmithingHelper.upgradesBodyCard(this.reinforcementMaterial) ? 1 : 0);
        if (this.bodyState == null || this.bodyUpgradeCount != upgrades) {
            if (this.bodyState != null) this.bodyState.commit();
            AbstractCard live = SmithingHelper.effectiveBodyCard(this.bodyCard, this.reinforcementMaterial, ownUpgrades);
            live.uuid = this.uuid;
            this.bodyState = new SmithingBodyState(this.bodyCard, live);
            this.bodyUpgradeCount = upgrades;
            SmithingBody.bind(live, this);
        }
        return this.bodyState.card();
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
