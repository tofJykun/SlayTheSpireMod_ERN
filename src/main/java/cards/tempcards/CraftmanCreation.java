package cards.tempcards;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.SmithingHelper;
import patches.ReplayField;

import java.util.ArrayList;

public class CraftmanCreation extends CustomCard {
    public static final String ID = "CraftmanCreation";
    private static final String IMG_PATH = "img/cards/tempcards/CraftmanCreation.png";

    private AbstractCard infusionMaterial;
    private AbstractCard bodyCard;
    private AbstractCard reinforcementMaterial;
    private AbstractCard pendingInfusionMaterial;
    private boolean needsPostUseRefresh;

    public CraftmanCreation() {
        this(null, null, null);
    }

    public CraftmanCreation(AbstractCard infusionMaterial, AbstractCard bodyCard, AbstractCard reinforcementMaterial) {
        super(ID, getCardStrings().NAME, IMG_PATH,
                SmithingHelper.defaultCraftmanCost(bodyCard, infusionMaterial, reinforcementMaterial),
                getCardStrings().DESCRIPTION,
                bodyCard == null ? CardType.SKILL : bodyCard.type,
                CardColor.COLORLESS, CardRarity.SPECIAL,
                SmithingHelper.combinedTarget(infusionMaterial, bodyCard, reinforcementMaterial));
        this.infusionMaterial = copyCard(infusionMaterial);
        this.bodyCard = copyCard(bodyCard);
        this.reinforcementMaterial = copyCard(reinforcementMaterial);
        syncFromParts();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        SmithingHelper.playComponent(
                SmithingHelper.effectiveInfusionMaterial(this.infusionMaterial, this.reinforcementMaterial), p, m);
        SmithingHelper.playComponent(effectiveBodyCard(), p, m);
        SmithingHelper.playComponent(effectiveReinforcementMaterial(), p, m);
        if (SmithingHelper.randomizesInfusionMaterialAfterUse(this.reinforcementMaterial)) {
            this.pendingInfusionMaterial = SmithingHelper.randomInfusionMaterial();
            this.needsPostUseRefresh = true;
        }
    }

    public void refreshAfterUseAction() {
        if (this.needsPostUseRefresh) {
            this.needsPostUseRefresh = false;
            this.infusionMaterial = copyCard(this.pendingInfusionMaterial);
            this.pendingInfusionMaterial = null;
            syncFromParts();
        }
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
        this.type = effectiveBody == null ? CardType.SKILL : effectiveBody.type;
        this.target = SmithingHelper.combinedTarget(effectiveInfusion, effectiveBody, effectiveReinforcement);
        this.exhaust = SmithingHelper.shouldExhaust(effectiveInfusion, effectiveBody, effectiveReinforcement);
        this.selfRetain = hasSelfRetain(effectiveInfusion) || hasSelfRetain(effectiveBody)
                || hasSelfRetain(effectiveReinforcement);
        this.isEthereal = hasEthereal(effectiveInfusion) || hasEthereal(effectiveBody)
                || hasEthereal(effectiveReinforcement);
        ReplayField.setReplay(this,
                SmithingHelper.replayAmount(effectiveInfusion, effectiveBody, effectiveReinforcement));
        updateDynamicDescription();
    }

    private void updateDynamicDescription() {
        CardStrings strings = getCardStrings();
        if (this.bodyCard == null) {
            this.rawDescription = strings.DESCRIPTION;
            setCardPreviews();
        } else {
            AbstractCard displayInfusion = effectiveInfusionMaterial();
            AbstractCard displayBody = effectiveBodyCard();
            AbstractCard displayReinforcement = this.reinforcementMaterial;
            StringBuilder builder = new StringBuilder();
            appendCardName(builder, displayInfusion);
            appendCardName(builder, displayBody);
            appendCardName(builder, displayReinforcement);
            this.rawDescription = builder.toString();
            setCardPreviews(displayInfusion, displayBody, displayReinforcement);
        }
        initializeDescription();
    }

    private void setCardPreviews(AbstractCard... previews) {
        this.cardsToPreview = null;
        MultiCardPreview.clear(this);
        ArrayList<AbstractCard> cards = new ArrayList<>();
        for (AbstractCard preview : previews) {
            AbstractCard copy = copyCard(preview);
            if (copy != null) {
                cards.add(copy);
            }
        }
        if (cards.size() == 1) {
            this.cardsToPreview = cards.get(0);
        } else if (cards.size() > 1) {
            MultiCardPreview.add(this, true, cards.toArray(new AbstractCard[0]));
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
