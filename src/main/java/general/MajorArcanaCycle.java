package general;

public final class MajorArcanaCycle {
    private static long combat;
    private static int nextStage = 1;

    private MajorArcanaCycle() {}

    public static long combat() { return combat; }
    public static int nextStage() { return nextStage; }
    public static void resetCombat() { combat++; nextStage = 1; }
    public static void afterPlay() { nextStage = nextStage % 22 + 1; }
}
