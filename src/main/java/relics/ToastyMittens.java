package relics;

import basemod.abstracts.CustomRelic;
import cards.wylder.PitaBread;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ToastyMittens extends CustomRelic {
    public static final String ID = "ToastyMittens";
    private static final String IMG = "img/relics/wylder/ToastyMittens.png";
    private static final String IMG_OTL = "img/relics/wylder/outline/ToastyMittens.png";

    public ToastyMittens() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.BOSS, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public void atTurnStart() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new PitaBread(), 1, false));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ToastyMittens();
    }
}
