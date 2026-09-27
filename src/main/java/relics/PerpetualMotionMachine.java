package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class PerpetualMotionMachine extends CustomRelic {
    public static final String ID = "PerpetualMotionMachine";
    private static final String IMG = "img/relics/undertaker/PerpetualMotionMachine.png";
    private static final String IMG_OTL = "img/relics/undertaker/outline/PerpetualMotionMachine.png";
    private static final int DRAW = 2;
    private boolean triggeredThisTurn;

    public PerpetualMotionMachine() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.BOSS, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        this.triggeredThisTurn = false;
    }

    @Override
    public void atTurnStart() {
        this.triggeredThisTurn = false;
    }

    @Override
    public void onManualDiscard() {
        if (this.triggeredThisTurn || AbstractDungeon.player == null) {
            return;
        }
        this.triggeredThisTurn = true;
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, DRAW));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new PerpetualMotionMachine();
    }
}
