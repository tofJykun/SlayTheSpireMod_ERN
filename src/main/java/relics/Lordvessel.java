package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Lordvessel extends CustomRelic {
    public static final String ID = "Lordvessel";

    public Lordvessel() {
        super(ID, ImageMaster.loadImage("img/relics/scholar/Lordvessel.png"),
                ImageMaster.loadImage("img/relics/scholar/outline/Lordvessel.png"),
                RelicTier.BOSS, LandingSound.CLINK);
        setCounter(1);
    }

    public boolean consumeCharge() {
        if (counter <= 0) return false;
        flash();
        setCounter(0);
        return true;
    }

    @Override
    public void setCounter(int value) {
        counter = value > 0 ? 1 : 0;
        grayscale = counter == 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Lordvessel();
    }
}
