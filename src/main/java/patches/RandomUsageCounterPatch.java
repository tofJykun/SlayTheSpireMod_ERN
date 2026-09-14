package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.random.Random;
import general.RandomUsageCounter;

public final class RandomUsageCounterPatch {
    private RandomUsageCounterPatch() {
    }

    @SpirePatch(clz = Random.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {Long.class, int.class})
    public static class ConstructorWithCounterPatch {
        @SpirePrefixPatch
        public static void prefix(Random __instance, Long seed, int counter) {
            RandomUsageCounter.beginSuppress();
        }

        @SpirePostfixPatch
        public static void postfix(Random __instance, Long seed, int counter) {
            RandomUsageCounter.endSuppress();
        }
    }

    @SpirePatch(clz = Random.class, method = "setCounter", paramtypez = {int.class})
    public static class SetCounterPatch {
        @SpirePrefixPatch
        public static void prefix(Random __instance, int targetCounter) {
            RandomUsageCounter.beginSuppress();
        }

        @SpirePostfixPatch
        public static void postfix(Random __instance, int targetCounter) {
            RandomUsageCounter.endSuppress();
        }
    }

    @SpirePatch(clz = Random.class, method = "random", paramtypez = {int.class})
    public static class RandomIntRangePatch {
        @SpirePostfixPatch
        public static void postfix(Random __instance, int range) {
            RandomUsageCounter.recordRandomUse();
        }
    }

    @SpirePatch(clz = Random.class, method = "random", paramtypez = {int.class, int.class})
    public static class RandomIntStartEndPatch {
        @SpirePostfixPatch
        public static void postfix(Random __instance, int start, int end) {
            RandomUsageCounter.recordRandomUse();
        }
    }

    @SpirePatch(clz = Random.class, method = "random", paramtypez = {long.class})
    public static class RandomLongRangePatch {
        @SpirePostfixPatch
        public static void postfix(Random __instance, long range) {
            RandomUsageCounter.recordRandomUse();
        }
    }

    @SpirePatch(clz = Random.class, method = "random", paramtypez = {long.class, long.class})
    public static class RandomLongStartEndPatch {
        @SpirePostfixPatch
        public static void postfix(Random __instance, long start, long end) {
            RandomUsageCounter.recordRandomUse();
        }
    }

    @SpirePatch(clz = Random.class, method = "randomLong")
    public static class RandomLongPatch {
        @SpirePostfixPatch
        public static void postfix(Random __instance) {
            RandomUsageCounter.recordRandomUse();
        }
    }

    @SpirePatch(clz = Random.class, method = "randomBoolean", paramtypez = {})
    public static class RandomBooleanPatch {
        @SpirePostfixPatch
        public static void postfix(Random __instance) {
            RandomUsageCounter.recordRandomUse();
        }
    }

    @SpirePatch(clz = Random.class, method = "randomBoolean", paramtypez = {float.class})
    public static class RandomBooleanChancePatch {
        @SpirePostfixPatch
        public static void postfix(Random __instance, float chance) {
            RandomUsageCounter.recordRandomUse();
        }
    }

    @SpirePatch(clz = Random.class, method = "random", paramtypez = {})
    public static class RandomFloatPatch {
        @SpirePostfixPatch
        public static void postfix(Random __instance) {
            RandomUsageCounter.recordRandomUse();
        }
    }

    @SpirePatch(clz = Random.class, method = "random", paramtypez = {float.class})
    public static class RandomFloatRangePatch {
        @SpirePostfixPatch
        public static void postfix(Random __instance, float range) {
            RandomUsageCounter.recordRandomUse();
        }
    }

    @SpirePatch(clz = Random.class, method = "random", paramtypez = {float.class, float.class})
    public static class RandomFloatStartEndPatch {
        @SpirePostfixPatch
        public static void postfix(Random __instance, float start, float end) {
            RandomUsageCounter.recordRandomUse();
        }
    }
}
