package relics;

import basemod.abstracts.CustomRelic;
import basemod.helpers.CardPowerTip;
import cards.guardian.MarionetteArmor;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import java.util.Iterator;

public class TheTwelveBronzeColossi extends CustomRelic {
    public static final String ID = "TheTwelveBronzeColossi";
    private boolean awaitingConfirmation;

    public TheTwelveBronzeColossi() {
        super(ID, ImageMaster.loadImage("img/relics/guardian/TheTwelveBronzeColossi.png"),
                ImageMaster.loadImage("img/relics/guardian/outline/TheTwelveBronzeColossi.png"),
                RelicTier.BOSS, LandingSound.HEAVY);
        this.tips.add(new CardPowerTip(new MarionetteArmor()));
    }

    @Override
    public void onEquip() {
        int count = 0;
        for (Iterator<AbstractCard> it = AbstractDungeon.player.masterDeck.group.iterator(); it.hasNext();) {
            if (it.next().hasTag(AbstractCard.CardTags.STARTER_STRIKE)) {
                it.remove();
                count++;
            }
        }
        if (count == 0) {
            return;
        }

        CardGroup replacements = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (int i = 0; i < count; i++) {
            AbstractCard card = new MarionetteArmor();
            UnlockTracker.markCardAsSeen(card.cardID);
            card.isSeen = true;
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                relic.onPreviewObtainCard(card);
            }
            replacements.addToBottom(card);
        }
        // The confirmation grid obtains these cards; do not also add them to the deck here.
        awaitingConfirmation = true;
        AbstractDungeon.gridSelectScreen.openConfirmationGrid(replacements, DESCRIPTIONS[1]);
    }

    @Override
    public void update() {
        super.update();
        if (awaitingConfirmation && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.GRID) {
            awaitingConfirmation = false;
            AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.25F;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TheTwelveBronzeColossi();
    }
}
