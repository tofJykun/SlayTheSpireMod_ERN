package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import powers.HoverPower;

public class SoaringInsignia extends CustomRelic {
    public static final String ID = "SoaringInsignia";
    private static final String IMG = "img/relics/guardian/SoaringInsignia.png";
    private static final String IMG_OTL = "img/relics/guardian/outline/SoaringInsignia.png";
    private static final int HOVER = 6;

    public SoaringInsignia() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(HonorOfPinionfolk.ID);
    }

    @Override
    public void bossObtainLogic() {
        if (canSpawn()) {
            int slot = AbstractDungeon.player.relics.indexOf(AbstractDungeon.player.getRelic(HonorOfPinionfolk.ID));
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
                new HoverPower(AbstractDungeon.player, HOVER), HOVER));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoaringInsignia();
    }
}
