package relics;

import actions.WheelCatalystAction;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class WheelCatalyst extends CustomRelic {
    public static final String ID = "WheelCatalyst";
    private static final String IMG = "img/relics/recluse/WheelCatalyst.png";
    private static final String IMG_OTL = "img/relics/recluse/outline/WheelCatalyst.png";

    public WheelCatalyst() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.RARE, AbstractRelic.LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStartPreDraw() {
        flash();
        addToBot((AbstractGameAction)new RelicAboveCreatureAction((AbstractCreature)AbstractDungeon.player, this));
        addToBot((AbstractGameAction)new WheelCatalystAction());
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new WheelCatalyst();
    }
}
