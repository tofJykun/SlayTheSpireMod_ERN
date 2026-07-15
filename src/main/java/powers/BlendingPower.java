package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class BlendingPower extends AbstractPower {
    public static final String POWER_ID = "BlendingPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final Set<AbstractPower> SUPPRESSED_POWERS =
            Collections.newSetFromMap(new IdentityHashMap<AbstractPower, Boolean>());

    public BlendingPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount = -1;
        updateDescription();
    }

    public void onPostPowerApply(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power == null || source != this.owner || target == null || target.isPlayer || power.amount <= 0) {
            return;
        }
        if (SUPPRESSED_POWERS.remove(power)) {
            return;
        }

        if ("Poison".equals(power.ID)) {
            flash();
            applySuppressed(target, new ScarletRotPower(target, this.owner, power.amount), power.amount);
            applySuppressed(target, new BloodburnPower(target, this.owner, power.amount), power.amount);
        } else if (ScarletRotPower.POWER_ID.equals(power.ID)) {
            flash();
            applySuppressed(target, new PoisonPower(target, this.owner, power.amount), power.amount);
            applySuppressed(target, new BloodburnPower(target, this.owner, power.amount), power.amount);
        } else if (BloodburnPower.POWER_ID.equals(power.ID)) {
            flash();
            applySuppressed(target, new PoisonPower(target, this.owner, power.amount), power.amount);
            applySuppressed(target, new ScarletRotPower(target, this.owner, power.amount), power.amount);
        }
    }

    private void applySuppressed(AbstractCreature target, AbstractPower power, int amount) {
        SUPPRESSED_POWERS.add(power);
        addToBot((AbstractGameAction)new ApplyPowerAction(target, this.owner, power, amount,
                true, AbstractGameAction.AttackEffect.POISON));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}

