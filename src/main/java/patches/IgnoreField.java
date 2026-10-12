package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

public final class IgnoreField {
    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static final SpireField<Boolean> ignored = new SpireField<>(() -> false);
    }

    private IgnoreField() {}

    public static boolean isIgnored(AbstractCard card) {
        return card != null && Fields.ignored.get(card);
    }

    public static void ignore(AbstractCard card) {
        if (card != null && !isIgnored(card)) {
            Fields.ignored.set(card, true);
            card.initializeDescription();
        }
    }

    public static void inherit(AbstractCard destination, AbstractCard source) {
        if (isIgnored(source)) ignore(destination);
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class CopyPatch {
        @SpirePostfixPatch
        public static AbstractCard postfix(AbstractCard __result, AbstractCard __instance) {
            inherit(__result, __instance);
            return __result;
        }
    }
}
