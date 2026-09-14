package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.lang.reflect.Constructor;

public class OilPot extends CustomRelic {
    public static final String ID = "OilPot";
    private static final String IMG = "img/relics/executor/OilPot.png";
    private static final String IMG_OTL = "img/relics/executor/outline/OilPot.png";

    private boolean usedThisCombat = false;
    private boolean applyingDuplicate = false;

    public OilPot() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.UNCOMMON, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        this.usedThisCombat = false;
        this.applyingDuplicate = false;
    }

    public void onPostPowerApply(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (this.usedThisCombat || this.applyingDuplicate || power == null || target == null || source == null
                || !source.isPlayer || target.isPlayer || !(target instanceof AbstractMonster)
                || power.type != AbstractPower.PowerType.DEBUFF || power.amount <= 0
                || target.isDeadOrEscaped()) {
            return;
        }

        AbstractPower duplicate = makeDuplicatePower(power, target, source, power.amount);
        if (duplicate == null) {
            return;
        }

        this.usedThisCombat = true;
        this.applyingDuplicate = true;
        flash();
        addToBot((AbstractGameAction)new RelicAboveCreatureAction(target, this));
        addToBot((AbstractGameAction)new ApplyPowerAction(target, source, duplicate, power.amount));
        addToBot((AbstractGameAction)new AbstractGameAction() {
            @Override
            public void update() {
                OilPot.this.applyingDuplicate = false;
                this.isDone = true;
            }
        });
    }

    private AbstractPower makeDuplicatePower(AbstractPower original, AbstractCreature target,
                                            AbstractCreature source, int amount) {
        Class<? extends AbstractPower> powerClass = original.getClass();
        try {
            Constructor<? extends AbstractPower> constructor =
                    powerClass.getConstructor(AbstractCreature.class, AbstractCreature.class, int.class);
            return constructor.newInstance(target, source, amount);
        } catch (Exception ignored) {
        }
        try {
            Constructor<? extends AbstractPower> constructor =
                    powerClass.getConstructor(AbstractCreature.class, int.class, boolean.class);
            return constructor.newInstance(target, amount, !source.isPlayer);
        } catch (Exception ignored) {
        }
        try {
            Constructor<? extends AbstractPower> constructor =
                    powerClass.getConstructor(AbstractCreature.class, int.class);
            return constructor.newInstance(target, amount);
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new OilPot();
    }
}
