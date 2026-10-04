package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class InvigoratingCuredMeat extends CustomRelic {
    public static final String ID = "InvigoratingCuredMeat";
    private static final String IMG = "img/relics/executor/InvigoratingCuredMeat.png";
    private static final String OUTLINE = "img/relics/executor/outline/InvigoratingCuredMeat.png";
    private static final int MAX_HP = 7;

    public InvigoratingCuredMeat() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(OUTLINE),
                RelicTier.SHOP, LandingSound.FLAT);
    }

    @Override
    public void onRest() {
        flash();
        AbstractDungeon.player.increaseMaxHp(MAX_HP, true);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new InvigoratingCuredMeat();
    }
}
