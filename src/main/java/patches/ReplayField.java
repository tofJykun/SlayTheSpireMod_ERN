package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class ReplayField {
    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static SpireField<Integer> replay = new SpireField<Integer>(() -> 0);
        public static SpireField<Boolean> replayCopy = new SpireField<Boolean>(() -> false);
        public static SpireField<Integer> remainingReplay = new SpireField<Integer>(() -> 0);
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class CopyPatch {
        @SpirePostfixPatch
        public static AbstractCard postfix(AbstractCard __result, AbstractCard __instance) {
            setReplay(__result, getReplay(__instance));
            setReplayCopy(__result, isReplayCopy(__instance));
            setRemainingReplay(__result, getRemainingReplay(__instance));
            return __result;
        }
    }

    public static int getReplay(AbstractCard card) {
        return Fields.replay.get(card);
    }

    public static void setReplay(AbstractCard card, int amount) {
        Fields.replay.set(card, Math.max(0, amount));
    }

    public static boolean isReplayCopy(AbstractCard card) {
        return Fields.replayCopy.get(card);
    }

    public static void setReplayCopy(AbstractCard card, boolean value) {
        Fields.replayCopy.set(card, value);
    }

    public static int getRemainingReplay(AbstractCard card) {
        return Fields.remainingReplay.get(card);
    }

    public static void setRemainingReplay(AbstractCard card, int amount) {
        Fields.remainingReplay.set(card, Math.max(0, amount));
    }
}
