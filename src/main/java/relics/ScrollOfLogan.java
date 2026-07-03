package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.Locale;

public class ScrollOfLogan extends CustomRelic {
    public static final String ID = "ScrollOfLogan";
    private static final String IMG = "img/relics/recluse/ScrollOfLogan.png";
    private static final String IMG_OTL = "img/relics/recluse/outline/ScrollOfLogan.png";

    public ScrollOfLogan() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.SHOP, AbstractRelic.LandingSound.MAGICAL);
    }

    @Override
    public void atTurnStart() {
        AbstractCard card = getRandomCrystalCard();
        if (card != null) {
            flash();
            AbstractDungeon.actionManager.addToBottom(
                    new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(
                    new MakeTempCardInHandAction(card.makeCopy(), 1, false));
        }
    }

    private AbstractCard getRandomCrystalCard() {
        ArrayList<AbstractCard> candidates = new ArrayList<>();
        for (AbstractCard card : CardLibrary.getAllCards()) {
            if (isCrystalCard(card)
                    && card.type != AbstractCard.CardType.STATUS
                    && card.type != AbstractCard.CardType.CURSE) {
                candidates.add(card);
            }
        }

        if (candidates.isEmpty()) {
            return null;
        }
        return candidates.get(AbstractDungeon.cardRandomRng.random(candidates.size() - 1));
    }

    public static boolean isCrystalCard(AbstractCard card) {
        return card.name != null && containsCrystalText(card.name);
    }

    private static boolean containsCrystalText(String text) {
        return text.toLowerCase(Locale.ROOT).contains("crystal") || text.contains("\u7ed3\u6676");
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ScrollOfLogan();
    }
}
