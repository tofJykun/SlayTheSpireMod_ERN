package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import general.CrystalCardHelper;

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
        AbstractCard card = CrystalCardHelper.randomCrystalCard();
        if (card != null) {
            flash();
            AbstractDungeon.actionManager.addToBottom(
                    new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(
                    new MakeTempCardInHandAction(card.makeCopy(), 1, false));
        }
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
