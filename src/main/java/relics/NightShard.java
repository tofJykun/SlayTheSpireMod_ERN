package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class NightShard extends CustomRelic {
    public static final String ID = "NightShard";
    private static final String IMG = "img/relics/recluse/NightShard.png";
    private static final String IMG_OTL = "img/relics/recluse/outline/NightShard.png";
    private static final int MAX_HP = 1;
    private static boolean gainingMaxHp;

    public NightShard() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.UNCOMMON, AbstractRelic.LandingSound.MAGICAL);
    }

    public static boolean isGainingMaxHp() {
        return gainingMaxHp;
    }

    public void onActualHeal() {
        if (gainingMaxHp || AbstractDungeon.player == null) {
            return;
        }
        flash();
        gainingMaxHp = true;
        try {
            AbstractDungeon.player.increaseMaxHp(MAX_HP, true);
        } finally {
            gainingMaxHp = false;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new NightShard();
    }
}
