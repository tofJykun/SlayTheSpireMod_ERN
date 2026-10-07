package relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import general.SolarCardBlockHistory;

public class ShadowOfPorcine extends CustomRelic {
    public static final String ID = "ShadowOfPorcine";
    private static final String IMG = "img/relics/guardian/ShadowOfPorcine.png";
    private static final String IMG_OTL = "img/relics/guardian/outline/ShadowOfPorcine.png";
    private boolean available;

    public ShadowOfPorcine() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.COMMON, LandingSound.CLINK);
    }

    @Override
    public void atPreBattle() {
        available = true;
        grayscale = false;
    }

    @Override
    public int onPlayerGainedBlock(float blockAmount) {
        if (available && blockAmount > 0 && SolarCardBlockHistory.fromCard()) {
            available = false;
            grayscale = true;
            flash();
            return MathUtils.floor(blockAmount * 2);
        }
        return MathUtils.floor(blockAmount);
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
        return new ShadowOfPorcine();
    }
}
