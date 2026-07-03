package relics;

import basemod.abstracts.CustomRelic;
import cards.recluse.BedOfMagic;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class OolacileIvoryCatalyst extends CustomRelic {
    public static final String ID = "OolacileIvoryCatalyst";
    private static final String IMG = "img/relics/recluse/OolacileIvoryCatalyst.png";
    private static final String IMG_OTL = "img/relics/recluse/outline/OolacileIvoryCatalyst.png";
    private static final int BLOCK = 6;
    private static final int DRAW = 1;

    public OolacileIvoryCatalyst() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.BOSS, AbstractRelic.LandingSound.MAGICAL);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (BedOfMagic.ID.equals(card.cardID)) {
            flash();
            AbstractDungeon.actionManager.addToBottom(
                    new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(
                    new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, BLOCK));
            AbstractDungeon.actionManager.addToBottom(
                    new DrawCardAction(AbstractDungeon.player, DRAW));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new OolacileIvoryCatalyst();
    }
}
