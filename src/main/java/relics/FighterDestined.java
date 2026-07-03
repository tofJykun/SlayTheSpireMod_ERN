package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import powers.UnyieldingPlusPower;

public class FighterDestined extends CustomRelic {
    public static final String ID = "FighterDestined";
    private static final String IMG = "img/relics/raider/Retaliate.png";
    private static final String IMG_OTL = "img/relics/raider/outline/Retaliate.png";

    public FighterDestined() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.BOSS, AbstractRelic.LandingSound.HEAVY);
    }

    @Override
    public void bossObtainLogic() {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(FighterResolve.ID)) {
            instantObtain(AbstractDungeon.player, 0, true);
        } else {
            instantObtain();
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(FighterResolve.ID);
    }

    @Override
    public void atBattleStart() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction(
                (AbstractCreature)AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player,
                new UnyieldingPlusPower((AbstractCreature)AbstractDungeon.player, 1), 1));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FighterDestined();
    }
}
