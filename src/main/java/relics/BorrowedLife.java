package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class BorrowedLife extends CustomRelic {
    public static final String ID = "BorrowedLife";
    private static final String IMG = "img/relics/raider/BorrowedLife.png";
    private static final String IMG_OTL = "img/relics/raider/outline/BorrowedLife.png";

    public BorrowedLife() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.RARE, AbstractRelic.LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BorrowedLife();
    }
}
