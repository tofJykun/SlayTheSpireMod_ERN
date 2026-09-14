package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CovetousGoldSerpentRing extends CustomRelic {
    public static final String ID = "CovetousGoldSerpentRing";
    public static final int CHANCE = 25;
    private static final String IMG = "img/relics/ironeye/CovetousGoldSerpentRing.png";
    private static final String IMG_OTL = "img/relics/ironeye/outline/CovetousGoldSerpentRing.png";

    public CovetousGoldSerpentRing() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.UNCOMMON, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CovetousGoldSerpentRing();
    }
}
