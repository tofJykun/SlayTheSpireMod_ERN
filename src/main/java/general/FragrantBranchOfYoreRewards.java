package general;

public class FragrantBranchOfYoreRewards {
    private static int extraCardRewards;

    private FragrantBranchOfYoreRewards() {
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
