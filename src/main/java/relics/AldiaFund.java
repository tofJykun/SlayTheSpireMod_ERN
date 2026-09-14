package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class AldiaFund extends CustomRelic {
    public static final String ID = "AldiaFund";
    private static final String IMG = "img/relics/ironeye/AldiaFund.png";
    private static final String IMG_OTL = "img/relics/ironeye/outline/AldiaFund.png";
    private static final int ENERGY_GAIN = 1;

    private boolean triggeredThisTurn;

    public AldiaFund() {
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

    public void onTransformCard() {
        if (this.triggeredThisTurn) {
            return;
        }
        this.triggeredThisTurn = true;
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new GainEnergyAction(ENERGY_GAIN));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new AldiaFund();
    }
}
