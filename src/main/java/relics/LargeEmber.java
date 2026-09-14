package relics;

import basemod.abstracts.CustomRelic;
import basemod.helpers.CardPowerTip;
import cards.raider.Strike_Raider;
import cards.tempcards.CraftmanCreation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import patches.LargeEmberStrikePatch;

public class LargeEmber extends CustomRelic {
    public static final String ID = "LargeEmber";
    private static final String IMG = "img/relics/raider/LargeEmber.png";
    private static final String IMG_OTL = "img/relics/raider/outline/LargeEmber.png";
    public static final int DAMAGE_BONUS = 3;
    private final AbstractCard strikePreview;

    public LargeEmber() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.BOSS, AbstractRelic.LandingSound.HEAVY);
        this.strikePreview = new Strike_Raider();
        this.tips.add(new CardPowerTip(this.strikePreview));
    }

    @Override
    public float atDamageModify(float damage, AbstractCard card) {
        if (isActiveStrike(card)) {
            return damage + DAMAGE_BONUS;
        }
        return damage;
    }

    @Override
    public void update() {
        super.update();
        updateAllStrikeDescriptions();
    }

    @Override
    public void onEquip() {
        updateAllStrikeDescriptions();
    }

    @Override
    public void onUnequip() {
        LargeEmberStrikePatch.suppress(true);
        try {
            updateAllStrikeCards();
            LargeEmberStrikePatch.updateCard(this.strikePreview);
            LargeEmberStrikePatch.restoreAllKnown();
        } finally {
            LargeEmberStrikePatch.suppress(false);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new LargeEmber();
    }

    public static boolean isActive() {
        return !LargeEmberStrikePatch.isSuppressed()
                && AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(ID);
    }

    public static boolean isActiveStrike(AbstractCard card) {
        return isActive() && isStrike(card);
    }

    public static boolean isStrike(AbstractCard card) {
        return card != null && card.hasTag(AbstractCard.CardTags.STRIKE);
    }

    public static boolean isProtectedCard(AbstractCard card) {
        return isStrike(card) || card instanceof CraftmanCreation
                && ((CraftmanCreation)card).hasBodyTag(AbstractCard.CardTags.STRIKE);
    }

    public static int effectiveBaseCost(AbstractCard card) {
        if (isActiveStrike(card)) {
            return 0;
        }
        return card == null ? 0 : card.cost;
    }

    public static String removalDescriptionLine() {
        return com.megacrit.cardcrawl.core.Settings.language == com.megacrit.cardcrawl.core.Settings.GameLanguage.ZHS
                ? " NL 不能从牌组中移除。"
                : " NL Cannot be removed from your deck.";
    }

    private void updateAllStrikeDescriptions() {
        updateAllStrikeCards();
        LargeEmberStrikePatch.updateCard(this.strikePreview);
    }

    private void updateAllStrikeCards() {
        if (AbstractDungeon.player == null) {
            return;
        }
        updateGroup(AbstractDungeon.player.masterDeck);
        updateGroup(AbstractDungeon.player.hand);
        updateGroup(AbstractDungeon.player.drawPile);
        updateGroup(AbstractDungeon.player.discardPile);
        updateGroup(AbstractDungeon.player.exhaustPile);
    }

    private static void updateGroup(CardGroup group) {
        if (group == null) {
            return;
        }
        for (AbstractCard card : group.group) {
            if (card instanceof CraftmanCreation) {
                ((CraftmanCreation)card).refreshLargeEmber();
            } else if (isStrike(card)) {
                LargeEmberStrikePatch.updateCard(card);
            }
        }
    }
}
