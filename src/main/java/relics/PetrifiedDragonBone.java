package relics;

import actions.SmithingMaterialDiscoveryAction;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class PetrifiedDragonBone extends CustomRelic {
    public static final String ID = "PetrifiedDragonBone";
    private static final String IMG = "img/relics/raider/PetrifiedDragonBone.png";
    private static final String IMG_OTL = "img/relics/raider/outline/PetrifiedDragonBone.png";

    public PetrifiedDragonBone() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.UNCOMMON, AbstractRelic.LandingSound.HEAVY);
    }

    @Override
    public void atBattleStart() {
        flash();
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new RelicAboveCreatureAction(
                AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SmithingMaterialDiscoveryAction());
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new PetrifiedDragonBone();
    }
}
