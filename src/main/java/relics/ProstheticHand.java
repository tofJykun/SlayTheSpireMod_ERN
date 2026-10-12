package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ProstheticHand extends CustomRelic {
    public static final String ID = "ProstheticHand";

    public ProstheticHand() {
        super(ID, ImageMaster.loadImage("img/relics/duchess/ProstheticHand.png"),
                ImageMaster.loadImage("img/relics/duchess/outline/ProstheticHand.png"),
                RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ProstheticHand();
    }
}
