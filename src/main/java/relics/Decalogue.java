package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Decalogue extends CustomRelic {
    public static final String ID = "Decalogue";
    private static final String IMG = "img/relics/undertaker/Decalogue.png";
    private static final String IMG_OTL = "img/relics/undertaker/outline/Decalogue.png";

    public Decalogue() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.RARE, AbstractRelic.LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Decalogue();
    }
}
