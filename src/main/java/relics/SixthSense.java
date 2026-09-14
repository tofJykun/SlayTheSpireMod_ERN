package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import general.CombatState;

public class SixthSense extends CustomRelic {
    public static final String ID = "SixthSense";
    private static final String IMG = "img/relics/wylder/SixthSense.png";
    private static final String IMG_OTL = "img/relics/wylder/outline/SixthSense.png";
    private static final int DRAW_NEXT_TURN = 2;
    private static final int BLOCK_NEXT_TURN = 5;

    public SixthSense() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.STARTER, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void wasHPLost(int damageAmount) {
        if (this.counter == 0
                && damageAmount > 0
                && CombatState.isInCombat()) {
            this.counter = 1;
            flash();
            AbstractDungeon.actionManager.addToBottom(
                    new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction((AbstractCreature)AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player,
                            new DrawCardNextTurnPower((AbstractCreature)AbstractDungeon.player, DRAW_NEXT_TURN),
                            DRAW_NEXT_TURN));
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction((AbstractCreature)AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player,
                            new NextTurnBlockPower((AbstractCreature)AbstractDungeon.player, BLOCK_NEXT_TURN),
                            BLOCK_NEXT_TURN));
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SixthSense();
    }
}
