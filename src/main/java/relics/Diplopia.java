package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import powers.DiplopiaPower;

public class Diplopia extends CustomRelic {
    public static final String ID = "Diplopia";
    private static final String IMG = "img/relics/undertaker/Diplopia.png";
    private static final String IMG_OTL = "img/relics/undertaker/outline/Diplopia.png";

    public Diplopia() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.BOSS, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void bossObtainLogic() {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(Trance.ID)) {
            int slot = AbstractDungeon.player.relics.indexOf(AbstractDungeon.player.getRelic(Trance.ID));
            instantObtain(AbstractDungeon.player, slot, true);
        } else {
            instantObtain();
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(Trance.ID);
    }

    @Override
    public void atTurnStart() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                (AbstractCreature) AbstractDungeon.player,
                (AbstractCreature) AbstractDungeon.player,
                (AbstractPower) new DiplopiaPower((AbstractCreature) AbstractDungeon.player), 1, true));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Diplopia();
    }
}
