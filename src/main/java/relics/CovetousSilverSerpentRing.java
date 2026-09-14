package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CovetousSilverSerpentRing extends CustomRelic {
    public static final String ID = "CovetousSilverSerpentRing";
    private static final String IMG = "img/relics/ironeye/CovetousSilverSerpentRing.png";
    private static final String IMG_OTL = "img/relics/ironeye/outline/CovetousSilverSerpentRing.png";

    public CovetousSilverSerpentRing() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.UNCOMMON, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CovetousSilverSerpentRing();
    }
}
