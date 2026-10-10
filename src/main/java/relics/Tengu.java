package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Tengu extends CustomRelic {
    public static final String ID = "Tengu";
    private static final int STAT = 3;
    private static final int THRESHOLD = 4;
    private boolean available;

    public Tengu() {
        super(ID, ImageMaster.loadImage("img/relics/executor/Tengu.png"),
                ImageMaster.loadImage("img/relics/executor/outline/Tengu.png"),
                RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(Suncatcher.ID);
    }

    @Override
    public void bossObtainLogic() {
        if (canSpawn()) {
            int slot = AbstractDungeon.player.relics.indexOf(AbstractDungeon.player.getRelic(Suncatcher.ID));
            instantObtain(AbstractDungeon.player, slot, true);
        } else {
            instantObtain();
        }
    }

    @Override
    public void atPreBattle() {
        this.counter = 0;
        this.available = true;
        this.grayscale = false;
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new DexterityPower(AbstractDungeon.player, STAT), STAT));
    }

    public void onDebuffApplied() {
        if (!this.available) {
            return;
        }
        this.counter++;
        if (this.counter == THRESHOLD) {
            this.available = false;
            this.grayscale = true;
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new StrengthPower(AbstractDungeon.player, STAT), STAT));
        }
    }

    @Override
    public void onVictory() {
        this.available = false;
        this.counter = -1;
        this.grayscale = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Tengu();
    }
}
