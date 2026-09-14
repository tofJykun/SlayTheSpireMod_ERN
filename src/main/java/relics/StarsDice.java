package relics;

import actions.StarsDiceAction;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class StarsDice extends CustomRelic {
    public static final String ID = "StarsDice";
    private static final String IMG = "img/relics/executor/StarsDice.png";
    private static final String IMG_OTL = "img/relics/executor/outline/StarsDice.png";

    public StarsDice() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.COMMON, AbstractRelic.LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        if (AbstractDungeon.player == null || AbstractDungeon.player.hand.isEmpty()) {
            return;
        }
        flash();
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new RelicAboveCreatureAction(
                AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new StarsDiceAction());
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new StarsDice();
    }
}
