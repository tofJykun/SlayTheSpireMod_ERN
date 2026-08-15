package relics;

import basemod.abstracts.CustomRelic;
import basemod.helpers.CardPowerTip;
import cards.recluse.BedOfMagic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class HiddenMagicCocktail extends CustomRelic {
    public static final String ID = "HiddenMagicCocktail";
    private static final String IMG = "img/relics/recluse/HiddenMagicCocktail.png";
    private static final String IMG_OTL = "img/relics/recluse/outline/HiddenMagicCocktail.png";

    public HiddenMagicCocktail() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.BOSS, AbstractRelic.LandingSound.CLINK);
        BedOfMagic preview = new BedOfMagic();
        preview.upgrade();
        this.tips.add(new CardPowerTip(preview));
    }

    @Override
    public void bossObtainLogic() {
        instantObtain(AbstractDungeon.player, 0, true);
    }

    @Override
    public void atBattleStartPreDraw() {
        BedOfMagic bedOfMagic = new BedOfMagic();
        bedOfMagic.upgrade();
        flash();
        AbstractDungeon.actionManager.addToBottom(
                new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(
                new MakeTempCardInHandAction(bedOfMagic, 1, false));
    }

    @Override
    public void onEquip() {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(MagicCocktail.ID)) {
            AbstractDungeon.player.loseRelic(MagicCocktail.ID);
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(MagicCocktail.ID);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HiddenMagicCocktail();
    }
}
