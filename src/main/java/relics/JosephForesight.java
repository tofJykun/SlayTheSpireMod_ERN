package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import general.CombatState;

public class JosephForesight extends CustomRelic {
    public static final String ID = "JosephForesight";
    private static final String IMG = "img/relics/wylder/JosephForesight.png";
    private static final String IMG_OTL = "img/relics/wylder/outline/JosephForesight.png";
    private static final int DRAW_NEXT_TURN = 5;
    private static final int ENERGY_NEXT_TURN = 2;

    public JosephForesight() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.BOSS, AbstractRelic.LandingSound.MAGICAL);
    }

    @Override
    public void bossObtainLogic() {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SixthSense.ID)) {
            instantObtain(AbstractDungeon.player, 0, true);
        } else {
            instantObtain();
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SixthSense.ID);
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
                            new EnergizedPower((AbstractCreature)AbstractDungeon.player, ENERGY_NEXT_TURN),
                            ENERGY_NEXT_TURN));
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
        return new JosephForesight();
    }
}
