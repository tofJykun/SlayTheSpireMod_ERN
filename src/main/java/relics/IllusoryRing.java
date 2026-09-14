package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class IllusoryRing extends CustomRelic {
    public static final String ID = "IllusoryRing";
    private static final String IMG = "img/relics/duchess/IllusoryRing.png";
    private static final String IMG_OTL = "img/relics/duchess/outline/IllusoryRing.png";

    public IllusoryRing() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.COMMON, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new IllusoryRing();
    }
}
