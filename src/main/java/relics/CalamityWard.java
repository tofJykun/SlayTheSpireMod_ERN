package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CalamityWard extends CustomRelic {
    public static final String ID = "CalamityWard";
    private static final String IMG = "img/relics/executor/CalamityWard.png";
    private static final String IMG_OTL = "img/relics/executor/outline/CalamityWard.png";
    private static final int TRIGGER_TURN = 2;
    private static final int ARTIFACT = 1;

    public CalamityWard() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.STARTER, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void atPreBattle() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        if (this.counter < 0) {
            return;
        }
        this.counter++;
        if (this.counter == TRIGGER_TURN) {
            flash();
            AbstractDungeon.actionManager.addToBottom(
                    new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction((AbstractCreature)AbstractDungeon.player,
                            (AbstractCreature)AbstractDungeon.player,
                            new ArtifactPower((AbstractCreature)AbstractDungeon.player, ARTIFACT), ARTIFACT));
            this.counter = -1;
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
        return new CalamityWard();
    }
}
