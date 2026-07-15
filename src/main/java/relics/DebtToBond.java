package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import powers.GreyHealthPlusPower;
import powers.GreyHealthPower;

public class DebtToBond extends CustomRelic {
    public static final String ID = "DebtToBond";
    private static final String IMG = "img/relics/raider/DebtToBond.png";
    private static final String IMG_OTL = "img/relics/raider/outline/DebtToBond.png";
    private static final int ENERGY = 1;

    public DebtToBond() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.BOSS, AbstractRelic.LandingSound.HEAVY);
    }

    @Override
    public void atTurnStart() {
        if (hasGreyHealth()) {
            flash();
            AbstractDungeon.actionManager.addToBottom(
                    new RelicAboveCreatureAction((AbstractCreature)AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY));
        }
    }

    private boolean hasGreyHealth() {
        if (AbstractDungeon.player == null) {
            return false;
        }

        AbstractPower greyHealth = AbstractDungeon.player.getPower(GreyHealthPower.POWER_ID);
        if (greyHealth != null && greyHealth.amount > 0) {
            return true;
        }

        AbstractPower greyHealthPlus = AbstractDungeon.player.getPower(GreyHealthPlusPower.POWER_ID);
        return greyHealthPlus != null && greyHealthPlus.amount > 0;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DebtToBond();
    }
}
