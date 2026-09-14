package relics;

import actions.TransposingKilnAction;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class TransposingKiln extends CustomRelic {
    public static final String ID = "TransposingKiln";
    private static final String IMG = "img/relics/wylder/TransposingKiln.png";
    private static final String IMG_OTL = "img/relics/wylder/outline/TransposingKiln.png";
    private boolean activated;

    public TransposingKiln() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.UNCOMMON, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public void atBattleStartPreDraw() {
        this.activated = false;
    }

    @Override
    public void atTurnStartPostDraw() {
        if (!this.activated) {
            this.activated = true;
            AbstractDungeon.actionManager.addToBottom(new TransposingKilnAction(this));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TransposingKiln();
    }
}
