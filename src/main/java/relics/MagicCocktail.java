package relics;

import basemod.abstracts.CustomRelic;
import cards.recluse.BedOfMagic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class MagicCocktail extends CustomRelic {
    public static final String ID = "MagicCocktail";
    private static final String IMG = "img/relics/recluse/MagicCocktail.png";
    private static final String IMG_OTL = "img/relics/recluse/outline/MagicCocktail.png";

    public MagicCocktail() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.STARTER, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void atBattleStartPreDraw() {
        flash();
        AbstractDungeon.actionManager.addToBottom(
                new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(
                new MakeTempCardInHandAction(new BedOfMagic(), 1, false));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MagicCocktail();
    }
}
