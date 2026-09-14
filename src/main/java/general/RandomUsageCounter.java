package general;

public final class RandomUsageCounter {
    private static int combatRandomUses = 0;
    private static boolean enabled = false;
    private static int suppressDepth = 0;

    private RandomUsageCounter() {
    }

    public static void resetCombat() {
        combatRandomUses = 0;
        enabled = true;
        suppressDepth = 0;
    }

    public static void endCombat() {
        enabled = false;
        suppressDepth = 0;
    }

    public static int getCombatRandomUses() {
        return combatRandomUses;
    }

    public static void recordRandomUse() {
        if (enabled && suppressDepth == 0 && CombatState.isInCombat()) {
            combatRandomUses++;
        }
    }

    public static void beginSuppress() {
        suppressDepth++;
    }

    public static void endSuppress() {
        if (suppressDepth > 0) {
            suppressDepth--;
        }
    }
}
