package patches;

import cards.AbstractScheduledCard;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class ScheduledKeywordPatch {
    private static void removeGenericTooltip(AbstractCard card) {
        if (card instanceof AbstractScheduledCard) {
            // Parsing already marked the word gold; retain only the live-count custom tooltip.
            card.keywords.remove("定时");
            card.keywords.remove("scheduled");
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
    public static class English {
        @SpirePostfixPatch
        public static void postfix(AbstractCard __instance) {
            removeGenericTooltip(__instance);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "initializeDescriptionCN")
    public static class Chinese {
        @SpirePostfixPatch
        public static void postfix(AbstractCard __instance) {
            removeGenericTooltip(__instance);
        }
    }
}
