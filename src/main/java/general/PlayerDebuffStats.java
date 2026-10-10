package general;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public final class PlayerDebuffStats {
    private static int applications;

    private PlayerDebuffStats() {}

    public static void resetCombat() {
        applications = 0;
    }

    public static void recordApplication(AbstractCreature target, AbstractPower power, int amount) {
        if (isPlayerDebuffApplication(target, power, amount)) {
            applications++;
        }
    }

    public static boolean isPlayerDebuffApplication(AbstractCreature target, AbstractPower power, int amount) {
        return CombatState.isInCombat() && target != null && target == AbstractDungeon.player
                && power != null && power.type == AbstractPower.PowerType.DEBUFF
                && amount != 0 && (amount > 0 || power.canGoNegative);
    }

    public static int getApplications() {
        return CombatState.isInCombat() ? applications : 0;
    }
}
