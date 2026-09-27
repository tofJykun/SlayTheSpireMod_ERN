package general;

public final class ExtraCardRewards {
    private static int extraCardRewards;

    private ExtraCardRewards() {
    }

    public static void resetCombat() {
        extraCardRewards = 0;
    }

    public static void add(int amount) {
        if (amount > 0) {
            extraCardRewards += amount;
        }
    }

    public static int consume() {
        int amount = extraCardRewards;
        extraCardRewards = 0;
        return amount;
    }
}
