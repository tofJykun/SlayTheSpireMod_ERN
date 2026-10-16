package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class BeckoningCat extends CustomRelic {
    public static final String ID = "BeckoningCat";

    public BeckoningCat() {
        super(ID, ImageMaster.loadImage("img/relics/ironeye/BeckoningCat.png"),
                ImageMaster.loadImage("img/relics/ironeye/outline/BeckoningCat.png"),
                RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public void onEquip() {
        this.counter = 0;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.rarity != AbstractCard.CardRarity.RARE) return;
        this.counter++;
        if (this.counter == 2) {
            this.counter = 0;
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new DrawCardAction(AbstractDungeon.player, 1));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BeckoningCat();
    }
}
