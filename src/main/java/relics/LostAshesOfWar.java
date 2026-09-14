package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class LostAshesOfWar extends CustomRelic {
    public static final String ID = "LostAshesOfWar";
    private static final String IMG = "img/relics/ironeye/LostAshesOfWar.png";
    private static final String IMG_OTL = "img/relics/ironeye/outline/LostAshesOfWar.png";

    public LostAshesOfWar() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.RARE, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.counter != 0 || card == null) {
            return;
        }
        this.counter = 1;
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractCard copy = card.makeStatEquivalentCopy();
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new MakeTempCardInHandAction(copy, 1));
    }

    @Override
    public void onVictory() {
        this.counter = -1;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new LostAshesOfWar();
    }
}
