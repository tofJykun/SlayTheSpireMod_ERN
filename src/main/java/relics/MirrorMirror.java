package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class MirrorMirror extends CustomRelic {
    public static final String ID = "MirrorMirror";
    private static final String IMG = "img/relics/undertaker/MirrorMirror.png";
    private static final String IMG_OTL = "img/relics/undertaker/outline/MirrorMirror.png";

    public MirrorMirror() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.SHOP, AbstractRelic.LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MirrorMirror();
    }
}
