package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.lang.reflect.Constructor;

public class InfectionStrikePower extends AbstractPower {
    public static final String POWER_ID = "InfectionStrikePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private final boolean propagateSelfApplication;

    public InfectionStrikePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.propagateSelfApplication = owner != null && owner.hasPower(POWER_ID);
        this.type = PowerType.DEBUFF;
        this.isTurnBased = false;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateDescription();
    }

    public void onPostPowerApply(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power == null || target != this.owner || target.isPlayer || target.isDeadOrEscaped()
                || power.type != PowerType.DEBUFF || power.amount <= 0 || this.amount <= 0) {
            return;
        }
        if (POWER_ID.equals(power.ID) && (!(power instanceof InfectionStrikePower)
                || !((InfectionStrikePower)power).propagateSelfApplication)) {
            return;
        }

        int appliedAmount = power.amount;
        AbstractCreature actualSource = source != null ? source : AbstractDungeon.player;
        flash();
        consumeOneStack();

        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster == target || monster.isDeadOrEscaped()) {
                continue;
            }
            AbstractPower duplicate = makeDuplicatePower(power, monster, actualSource, appliedAmount);
            if (duplicate != null) {
                addToBot((AbstractGameAction)new ApplyPowerAction(monster, actualSource,
                        duplicate, appliedAmount));
            }
        }
    }

    private void consumeOneStack() {
        --this.amount;
        if (this.amount <= 0) {
            this.amount = 0;
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            updateDescription();
        }
    }

    public static AbstractPower makeDuplicatePower(AbstractPower original, AbstractCreature target,
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
            return constructor.newInstance(target, amount, source != null && !source.isPlayer);
        } catch (Exception ignored) {
        }
        try {
            Constructor<? extends AbstractPower> constructor =
                    powerClass.getConstructor(AbstractCreature.class, int.class);
            return constructor.newInstance(target, amount);
        } catch (Exception ignored) {
        }
        try {
            Constructor<? extends AbstractPower> constructor =
                    powerClass.getConstructor(AbstractCreature.class, AbstractCreature.class);
            return constructor.newInstance(target, source);
        } catch (Exception ignored) {
        }
        try {
            Constructor<? extends AbstractPower> constructor =
                    powerClass.getConstructor(AbstractCreature.class);
            return constructor.newInstance(target);
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
