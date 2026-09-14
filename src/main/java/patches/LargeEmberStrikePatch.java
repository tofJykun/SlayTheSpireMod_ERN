package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import relics.LargeEmber;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;

public class LargeEmberStrikePatch {
    private static final Map<AbstractCard, String> ORIGINAL_DESCRIPTIONS = new IdentityHashMap<AbstractCard, String>();
    private static final Map<AbstractCard, Integer> ORIGINAL_COST_FOR_TURN = new IdentityHashMap<AbstractCard, Integer>();
    private static final Map<AbstractCard, Boolean> ORIGINAL_COST_MODIFIED_FOR_TURN =
            new IdentityHashMap<AbstractCard, Boolean>();
    private static boolean suppressed;

    public static void suppress(boolean value) {
        suppressed = value;
    }

    public static boolean isSuppressed() {
        return suppressed;
    }

    public static void updateCard(AbstractCard card) {
        updateCost(card);
        if (card != null && card.rawDescription != null) {
            card.initializeDescription();
        }
    }

    public static void restoreAllKnown() {
        ArrayList<AbstractCard> cards = new ArrayList<AbstractCard>(ORIGINAL_COST_FOR_TURN.keySet());
        for (AbstractCard card : cards) {
            restoreCost(card);
        }
        ORIGINAL_DESCRIPTIONS.clear();
    }

    private static void updateCost(AbstractCard card) {
        if (card == null) {
            return;
        }
        if (!suppressed && LargeEmber.isActiveStrike(card)) {
            if (!ORIGINAL_COST_FOR_TURN.containsKey(card)) {
                ORIGINAL_COST_FOR_TURN.put(card, card.costForTurn);
                ORIGINAL_COST_MODIFIED_FOR_TURN.put(card, card.isCostModifiedForTurn);
            }
            if (card.costForTurn != 0) {
                card.costForTurn = 0;
                card.isCostModifiedForTurn = card.cost != 0;
            }
        } else {
            restoreCost(card);
        }
    }

    private static void restoreCost(AbstractCard card) {
        if (!ORIGINAL_COST_FOR_TURN.containsKey(card)) {
            return;
        }
        card.costForTurn = ORIGINAL_COST_FOR_TURN.remove(card);
        Boolean wasModified = ORIGINAL_COST_MODIFIED_FOR_TURN.remove(card);
        card.isCostModifiedForTurn = wasModified != null && wasModified;
    }

    @SpirePatch(clz = AbstractCard.class, method = "freeToPlay")
    public static class FreeToPlayPatch {
        @SpirePostfixPatch
        public static boolean postfix(boolean __result, AbstractCard __instance) {
            return __result || LargeEmber.isActiveStrike(__instance);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
    public static class DescriptionPatch {
        @SpirePrefixPatch
        public static void prefix(AbstractCard __instance) {
            if (!LargeEmber.isActive() || !LargeEmber.isProtectedCard(__instance)
                    || __instance.rawDescription == null) {
                return;
            }
            ORIGINAL_DESCRIPTIONS.put(__instance, __instance.rawDescription);
            __instance.rawDescription = __instance.rawDescription + LargeEmber.removalDescriptionLine();
        }

        @SpirePostfixPatch
        public static void postfix(AbstractCard __instance) {
            if (ORIGINAL_DESCRIPTIONS.containsKey(__instance)) {
                __instance.rawDescription = ORIGINAL_DESCRIPTIONS.remove(__instance);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "applyPowers")
    public static class ApplyPowersPatch {
        @SpirePostfixPatch
        public static void postfix(AbstractCard __instance) {
            updateCost(__instance);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class CalculateCardDamagePatch {
        @SpirePostfixPatch
        public static void postfix(AbstractCard __instance) {
            updateCost(__instance);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "resetAttributes")
    public static class ResetAttributesPatch {
        @SpirePostfixPatch
        public static void postfix(AbstractCard __instance) {
            updateCost(__instance);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "getPurgeableCards")
    public static class PurgeableCardsPatch {
        @SpirePostfixPatch
        public static CardGroup postfix(CardGroup __result) {
            if (!LargeEmber.isActive() || __result == null) {
                return __result;
            }
            for (int i = __result.group.size() - 1; i >= 0; i--) {
                if (LargeEmber.isProtectedCard(__result.group.get(i))) {
                    __result.group.remove(i);
                }
            }
            return __result;
        }
    }
}
