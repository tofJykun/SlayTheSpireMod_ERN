package relics;

import basemod.abstracts.CustomRelic;
import cards.wylder.WylderIntentHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SilverPendant extends CustomRelic {
    public static final String ID = "SilverPendant";
    private static final String IMG = "img/relics/wylder/SilverPendant.png";
    private static final String IMG_OTL = "img/relics/wylder/outline/SilverPendant.png";

    public SilverPendant() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.RARE, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        int block = WylderIntentHelper.totalIncomingAttackDamage(AbstractDungeon.player);
        if (block <= 0) {
            return;
        }
        flash();
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new RelicAboveCreatureAction(
                AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new GainBlockAction(
                (AbstractCreature)AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player, block));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SilverPendant();
    }
}
