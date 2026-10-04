package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import powers.BloodlossPower;
import powers.DeathblightPower;
import powers.FrostbitePower;
import powers.MadnessPower;
import powers.SleepPower;

public class SeedbedCurse extends CustomRelic {
    public static final String ID = "SeedbedCurse";
    private static final String IMG = "img/relics/executor/SeedbedCurse.png";
    private static final String IMG_OTL = "img/relics/executor/outline/SeedbedCurse.png";
    private static final int THRESHOLD = 10;
    private static final int DEATHBLIGHT = 1;

    public SeedbedCurse() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.UNCOMMON, AbstractRelic.LandingSound.MAGICAL);
        this.counter = 0;
    }

    public void onPostPowerApply(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power == null || target == null || source == null || !source.isPlayer || target.isPlayer
                || !isAberration(power.ID) || power.amount <= 0) {
            return;
        }

        this.counter += power.amount;
        while (this.counter >= THRESHOLD) {
            this.counter -= THRESHOLD;
            triggerDeathblight();
        }
        if (this.counter == THRESHOLD - 1) {
            beginPulse();
            this.pulse = true;
        } else {
            this.pulse = false;
        }
    }

    private void triggerDeathblight() {
        AbstractMonster monster = AbstractDungeon.getMonsters().getRandomMonster(null, true,
                AbstractDungeon.cardRandomRng);
        if (monster == null) {
            return;
        }

        flash();
        this.pulse = false;
        addToBot((AbstractGameAction)new RelicAboveCreatureAction((AbstractCreature)monster, this));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)monster,
                (AbstractCreature)AbstractDungeon.player,
                new DeathblightPower((AbstractCreature)monster, DEATHBLIGHT), DEATHBLIGHT));
    }

    private boolean isAberration(String powerId) {
        return FrostbitePower.POWER_ID.equals(powerId)
                || SleepPower.POWER_ID.equals(powerId)
                || MadnessPower.POWER_ID.equals(powerId)
                || BloodlossPower.POWER_ID.equals(powerId);
    }

    @Override
    public void atBattleStart() {
        if (this.counter == THRESHOLD - 1) {
            beginPulse();
            this.pulse = true;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SeedbedCurse();
    }
}
