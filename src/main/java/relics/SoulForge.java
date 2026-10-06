package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.AbstractSummonPower;

public class SoulForge extends CustomRelic {
    public static final String ID = "SoulForge";
    private static final String IMG = "img/relics/revenant/SoulForge.png";
    private static final String IMG_OTL = "img/relics/revenant/outline/SoulForge.png";
    private static final int ENERGY = 1;
    private boolean triggeredThisTurn;

    public SoulForge() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.BOSS, AbstractRelic.LandingSound.HEAVY);
    }

    @Override
    public void atBattleStart() {
        triggeredThisTurn = false;
    }

    @Override
    public void atTurnStart() {
        triggeredThisTurn = false;
    }

    public void onPostPowerApply(AbstractPower power, AbstractCreature target) {
        if (triggeredThisTurn || AbstractDungeon.player == null || target != AbstractDungeon.player
                || !AbstractSummonPower.isSummonPower(power)) return;
        triggeredThisTurn = true;
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulForge();
    }
}
