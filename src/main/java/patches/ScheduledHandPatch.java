package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

public final class ScheduledHandPatch {
    private ScheduledHandPatch() {
    }

    private static void resetIfHand(CardGroup group, AbstractCard card) {
        if (group != null && group.type == CardGroup.CardGroupType.HAND) {
            ScheduledField.reset(card);
        }
    }

    private static void enterIfHand(CardGroup group, AbstractCard card) {
        if (group != null && group.type == CardGroup.CardGroupType.HAND) {
            ScheduledField.enterHand(card);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "addToHand", paramtypez = { AbstractCard.class })
    public static class AddToHandPatch {
        @SpirePrefixPatch
        public static void prefix(CardGroup __instance, AbstractCard card) {
            enterIfHand(__instance, card);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "addToTop", paramtypez = { AbstractCard.class })
    public static class AddToTopPatch {
        @SpirePrefixPatch
        public static void prefix(CardGroup __instance, AbstractCard card) {
            enterIfHand(__instance, card);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "addToBottom", paramtypez = { AbstractCard.class })
    public static class AddToBottomPatch {
        @SpirePrefixPatch
        public static void prefix(CardGroup __instance, AbstractCard card) {
            enterIfHand(__instance, card);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "addToRandomSpot", paramtypez = { AbstractCard.class })
    public static class AddToRandomSpotPatch {
        @SpirePrefixPatch
        public static void prefix(CardGroup __instance, AbstractCard card) {
            enterIfHand(__instance, card);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "removeCard", paramtypez = { AbstractCard.class })
    public static class RemoveCardPatch {
        @SpirePrefixPatch
        public static void prefix(CardGroup __instance, AbstractCard card) {
            resetIfHand(__instance, card);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "resetCardBeforeMoving", paramtypez = { AbstractCard.class })
    public static class ResetBeforeMovingPatch {
        @SpirePrefixPatch
        public static void prefix(CardGroup __instance, AbstractCard card) {
            resetIfHand(__instance, card);
        }
    }
}
