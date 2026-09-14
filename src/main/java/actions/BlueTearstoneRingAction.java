package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.BlueTearstoneRingPower;
import powers.GreyHealthPlusPower;
import powers.GreyHealthPower;

public class BlueTearstoneRingAction extends AbstractGameAction {
    private static boolean combatEndChecked = false;

    public BlueTearstoneRingAction(AbstractCreature target) {
        this.target = target;
        this.source = target;
    }

    public static void resetCombatEndCheck() {
        combatEndChecked = false;
    }

    public static void triggerAtCombatEnd(AbstractCreature target) {
        if (combatEndChecked) {
            return;
        }
        combatEndChecked = true;
        triggerIfLethal(target, false);
    }

    public static void triggerIfLethal(AbstractCreature target) {
        triggerIfLethal(target, true);
    }

    private static void triggerIfLethal(AbstractCreature target, boolean removeEmptyPowers) {
        if (target == null || target.isDeadOrEscaped()) {
            return;
        }
        AbstractPower ring = target.getPower(BlueTearstoneRingPower.POWER_ID);
        if (ring == null || ring.amount <= 0) {
            return;
        }
        boolean triggered = false;
        triggered |= halveIfLethal(target, GreyHealthPower.POWER_ID, removeEmptyPowers);
        triggered |= halveIfLethal(target, GreyHealthPlusPower.POWER_ID, removeEmptyPowers);
        if (triggered) {
            reduceBlueTearstoneRing(target, removeEmptyPowers);
        }
    }

    @Override
    public void update() {
        triggerIfLethal(this.target);
        this.isDone = true;
    }

    private static boolean halveIfLethal(AbstractCreature target, String powerId, boolean removeEmptyPowers) {
        AbstractPower power = target.getPower(powerId);
        if (power == null || power.amount < target.currentHealth) {
            return false;
        }

        power.amount = power.amount / 2;
        if (power.amount <= 0 && removeEmptyPowers) {
            target.powers.remove(power);
        } else {
            power.updateDescription();
        }
        return true;
    }

    private static void reduceBlueTearstoneRing(AbstractCreature target, boolean removeEmptyPowers) {
        AbstractPower power = target.getPower(BlueTearstoneRingPower.POWER_ID);
        if (power == null) {
            return;
        }
        power.flash();
        power.amount--;
        if (power.amount <= 0 && removeEmptyPowers) {
            target.powers.remove(power);
        } else {
            power.updateDescription();
        }
    }
}
