package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class FragileKeywordPatch {
    private static void removeGenericTooltip(AbstractCard card) {
        if (FragileField.isFragile(card)) {
            // Keep parsed gold text, but let FragileModifier provide the live-count tooltip.
            card.keywords.remove("易损");
            card.keywords.remove("fragile");
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
    public static class English {
        @SpirePostfixPatch
        public static void postfix(AbstractCard __instance) { removeGenericTooltip(__instance); }
    }

    @SpirePatch(clz = AbstractCard.class, method = "initializeDescriptionCN")
    public static class Chinese {
        @SpirePostfixPatch
        public static void postfix(AbstractCard __instance) { removeGenericTooltip(__instance); }
    }
}
