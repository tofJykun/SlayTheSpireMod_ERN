package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class LaplaceDemon extends CustomRelic {
    public static final String ID = "LaplaceDemon";
    private static final String IMG = "img/relics/undertaker/LaplaceDemon.png";
    private static final String IMG_OTL = "img/relics/undertaker/outline/LaplaceDemon.png";

    public LaplaceDemon() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.UNCOMMON, AbstractRelic.LandingSound.MAGICAL);
    }

    public String getExhaustPrompt() {
        return this.DESCRIPTIONS[1];
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new LaplaceDemon();
    }
}
