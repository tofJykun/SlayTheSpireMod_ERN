package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class FlintAndSteel extends CustomRelic {
    public static final String ID = "FlintAndSteel";

    public FlintAndSteel() {
        super(ID, ImageMaster.loadImage("img/relics/duchess/FlintAndSteel.png"),
                ImageMaster.loadImage("img/relics/duchess/outline/FlintAndSteel.png"),
                RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public void atPreBattle() {
        atTurnStart();
    }

    @Override
    public void atTurnStart() {
        this.counter = 0;
        this.grayscale = false;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.counter < 0 || this.counter >= 3) return;
        this.counter++;
        if (this.counter == 3) {
            this.grayscale = true;
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new GainEnergyAction(1));
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        this.grayscale = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FlintAndSteel();
    }
}
