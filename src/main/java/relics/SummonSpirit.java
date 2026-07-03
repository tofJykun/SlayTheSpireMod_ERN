package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import powers.SummonFrederick;
import powers.SummonHelen;
import powers.SummonSebastian;

public class SummonSpirit extends CustomRelic {
    public static final String ID = "SummonSpirit";
    private static final String IMG = "img/relics/revenant/SummonSpirit.png";
    private static final String IMG_OTL = "img/relics/revenant/outline/SummonSpirit.png";
    private static final int SUMMON_TURNS = 3;

    public SummonSpirit() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.STARTER, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void atPreBattle() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        if (this.counter < 0 || this.counter >= SUMMON_TURNS) {
            return;
        }

        this.counter++;
        flash();
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new RelicAboveCreatureAction(
                (AbstractCreature)AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction(
                (AbstractCreature)AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player,
                getSummonForTurn(this.counter)));

        if (this.counter >= SUMMON_TURNS) {
            this.counter = -1;
        }
    }

    private AbstractPower getSummonForTurn(int turn) {
        if (turn == 1) {
            return new SummonHelen((AbstractCreature)AbstractDungeon.player);
        }
        if (turn == 2) {
            return new SummonFrederick((AbstractCreature)AbstractDungeon.player);
        }
        return new SummonSebastian((AbstractCreature)AbstractDungeon.player);
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
        return new SummonSpirit();
    }
}
