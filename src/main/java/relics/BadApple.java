package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class BadApple extends CustomRelic {
    public static final String ID = "BadApple";
    private boolean available;

    public BadApple() {
        super(ID, ImageMaster.loadImage("img/relics/executor/BadApple.png"),
                ImageMaster.loadImage("img/relics/executor/outline/BadApple.png"),
                RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public void atPreBattle() {
        atTurnStart();
    }

    @Override
    public void atTurnStart() {
        available = true;
        grayscale = false;
    }

    public void onDebuffApplied() {
        if (!available) return;
        available = false;
        grayscale = true;
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new GainEnergyAction(1));
    }

    @Override
    public void onVictory() {
        available = false;
        grayscale = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BadApple();
    }
}
