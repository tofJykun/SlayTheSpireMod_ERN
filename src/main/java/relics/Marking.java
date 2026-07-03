package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Marking extends CustomRelic {
    public static final String ID = "Marking";
    private static final String IMG = "img/relics/ironeye/Marking.png";
    private static final String IMG_OTL = "img/relics/ironeye/outline/Marking.png";

    public Marking() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.STARTER, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.SKILL) {
            this.counter++;
            if (this.counter % 2 == 0) {
                this.counter = 0;
                flash();
                AbstractDungeon.actionManager.addToBottom(
                        new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractDungeon.actionManager.addToBottom(
                        new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 6));
            }
        }
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
        return new Marking();
    }
}
