package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class AnotherBottle extends CustomRelic {
    public static final String ID = "AnotherBottle";
    public static final int TOKEN_BONUS = 5;

    public AnotherBottle() {
        super(ID, ImageMaster.loadImage("img/relics/ironeye/AnotherBottle.png"),
                ImageMaster.loadImage("img/relics/ironeye/outline/AnotherBottle.png"),
                RelicTier.SHOP, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new AnotherBottle();
    }
}
