package general;

public final class SmithingStats {
    private static int combatSmithings;

    private SmithingStats() {
    }

    public static void resetCombat() {
        combatSmithings = 0;
    }

    public static void recordSmithing() {
        if (CombatState.isInCombat()) {
            combatSmithings++;
        }
    }

    public static int getCombatSmithings() {
        return CombatState.isInCombat() ? combatSmithings : 0;
    }
}
