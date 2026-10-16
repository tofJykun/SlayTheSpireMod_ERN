package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import powers.ArcanePower;

public class CompoundLens extends CustomRelic {
    public static final String ID = "CompoundLens";
    private static final int ARCANE = 12;

    public CompoundLens() {
        super(ID, ImageMaster.loadImage("img/relics/ironeye/CompoundLens.png"),
                ImageMaster.loadImage("img/relics/ironeye/outline/CompoundLens.png"),
                RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(EagleEye.ID);
    }

    @Override
    public void bossObtainLogic() {
        if (canSpawn()) {
            int slot = AbstractDungeon.player.relics.indexOf(AbstractDungeon.player.getRelic(EagleEye.ID));
            instantObtain(AbstractDungeon.player, slot, true);
        } else {
            instantObtain();
        }
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new ArcanePower(AbstractDungeon.player, ARCANE), ARCANE));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CompoundLens();
    }
}
