package patches;

import cards.AbstractScheduledCard;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

public final class ScheduledField {
    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static final SpireField<Integer> baseScheduled = new SpireField<Integer>(() -> 0);
        public static final SpireField<Integer> scheduled = new SpireField<Integer>(() -> 0);
        public static final SpireField<Boolean> pendingAutoplay = new SpireField<Boolean>(() -> false);
        public static final SpireField<Integer> handEntry = new SpireField<Integer>(() -> 0);
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class CopyPatch {
        @SpirePostfixPatch
        public static AbstractCard postfix(AbstractCard __result, AbstractCard __instance) {
            setBaseScheduled(__result, getBaseScheduled(__instance));
            setScheduled(__result, getScheduled(__instance));
            setPendingAutoplay(__result, false);
            Fields.handEntry.set(__result, 0);
            return __result;
        }
    }

    private ScheduledField() {
    }

    public static boolean isScheduled(AbstractCard card) {
        return card != null && getBaseScheduled(card) > 0;
    }

    public static int getBaseScheduled(AbstractCard card) {
        return card == null ? 0 : Fields.baseScheduled.get(card);
    }

    public static void setBaseScheduled(AbstractCard card, int amount) {
        if (card == null) {
            return;
        }
        int value = Math.max(0, amount);
        Fields.baseScheduled.set(card, value);
        Fields.scheduled.set(card, value);
        Fields.pendingAutoplay.set(card, false);
        notifyScheduledCountChanged(card);
        refreshDescription(card);
    }

    public static int getScheduled(AbstractCard card) {
        return card == null ? 0 : Fields.scheduled.get(card);
    }

    public static void setScheduled(AbstractCard card, int amount) {
        if (card == null) {
            return;
        }
        Fields.scheduled.set(card, Math.max(0, amount));
        notifyScheduledCountChanged(card);
        refreshDescription(card);
    }

    public static void reset(AbstractCard card) {
        if (!isScheduled(card)) {
            return;
        }
        Fields.scheduled.set(card, getBaseScheduled(card));
        Fields.pendingAutoplay.set(card, false);
        notifyScheduledCountChanged(card);
        refreshDescription(card);
    }

    public static void enterHand(AbstractCard card) {
        if (!isScheduled(card)) {
            return;
        }
        reset(card);
        Fields.handEntry.set(card, Fields.handEntry.get(card) + 1);
    }

    public static int getHandEntry(AbstractCard card) {
        return card == null ? 0 : Fields.handEntry.get(card);
    }

    public static boolean isPendingAutoplay(AbstractCard card) {
        return card != null && Fields.pendingAutoplay.get(card);
    }

    public static void setPendingAutoplay(AbstractCard card, boolean value) {
        if (card != null) {
            Fields.pendingAutoplay.set(card, value);
        }
    }

    private static void refreshDescription(AbstractCard card) {
        if (card.rawDescription != null) {
            card.initializeDescription();
        }
    }

    private static void notifyScheduledCountChanged(AbstractCard card) {
        if (card instanceof AbstractScheduledCard) {
            ((AbstractScheduledCard)card).onScheduledCountChanged(getScheduled(card), getBaseScheduled(card));
        }
    }
}
