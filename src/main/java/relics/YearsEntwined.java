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

public class YearsEntwined extends CustomRelic {
    public static final String ID = "YearsEntwined";
    private static final String IMG = "img/relics/revenant/YearsEntwined.png";
    private static final String IMG_OTL = "img/relics/revenant/outline/YearsEntwined.png";

    public YearsEntwined() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.BOSS, AbstractRelic.LandingSound.HEAVY);
    }

    @Override
    public void bossObtainLogic() {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SummonSpirit.ID)) {
            instantObtain(AbstractDungeon.player, 0, true);
        } else {
            instantObtain();
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SummonSpirit.ID);
    }

    @Override
    public void atTurnStart() {
        if (AbstractDungeon.player == null) {
            return;
        }

        flash();
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new RelicAboveCreatureAction(
                (AbstractCreature)AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction(
                (AbstractCreature)AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player,
                getRandomSummon()));
    }

    private AbstractPower getRandomSummon() {
        switch (AbstractDungeon.cardRandomRng.random(2)) {
            case 0:
                return new SummonHelen((AbstractCreature)AbstractDungeon.player);
            case 1:
                return new SummonFrederick((AbstractCreature)AbstractDungeon.player);
            default:
                return new SummonSebastian((AbstractCreature)AbstractDungeon.player);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new YearsEntwined();
    }
}
