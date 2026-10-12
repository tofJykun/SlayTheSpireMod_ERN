package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import powers.MushroomPower;

public class Colony extends CustomRelic {
    public static final String ID = "Colony";

    public Colony() {
        super(ID, ImageMaster.loadImage("img/relics/duchess/Colony.png"),
                ImageMaster.loadImage("img/relics/duchess/outline/Colony.png"),
                RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public void atPreBattle() {
        this.counter = 0;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.counter < 0) return;
        this.counter++;
        if (this.counter == 2) {
            this.counter = 0;
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new MushroomPower(AbstractDungeon.player), 1));
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Colony();
    }
}
