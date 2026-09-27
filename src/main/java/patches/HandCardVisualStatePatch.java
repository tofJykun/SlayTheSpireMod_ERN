package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.actions.unique.RestoreRetainedCardsAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/** Normalizes cards re-entering the hand after custom draw/return effects. */
public final class HandCardVisualStatePatch {
    private HandCardVisualStatePatch() {
    }

    private static void normalize(CardGroup group, AbstractCard card) {
        if (group == null || group.type != CardGroup.CardGroupType.HAND || card == null) {
            return;
        }

        card.unhover();
        card.untip();
        card.stopGlowing();
        card.unfadeOut();
        card.lighten(true);

        // The native restore action owns a live iterator over limbo and removes
        // each retained card itself after hand.addToTop returns.
        if (AbstractDungeon.actionManager != null
                && AbstractDungeon.actionManager.currentAction instanceof RestoreRetainedCardsAction) {
            return;
        }

        // A card left in limbo is rendered a second time over the hand card.
        if (AbstractDungeon.player != null && AbstractDungeon.player.limbo != null) {
            while (AbstractDungeon.player.limbo.group.remove(card)) {
                // Keep exactly one render owner for the card.
            }
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "addToHand",
            paramtypez = {AbstractCard.class})
    public static class AddToHandPatch {
        @SpirePrefixPatch
        public static void prefix(CardGroup __instance, AbstractCard card) {
            normalize(__instance, card);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "addToTop",
            paramtypez = {AbstractCard.class})
    public static class AddToTopPatch {
        @SpirePrefixPatch
        public static void prefix(CardGroup __instance, AbstractCard card) {
            normalize(__instance, card);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "addToBottom",
            paramtypez = {AbstractCard.class})
    public static class AddToBottomPatch {
        @SpirePrefixPatch
        public static void prefix(CardGroup __instance, AbstractCard card) {
            normalize(__instance, card);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "addToRandomSpot",
            paramtypez = {AbstractCard.class})
    public static class AddToRandomSpotPatch {
        @SpirePrefixPatch
        public static void prefix(CardGroup __instance, AbstractCard card) {
            normalize(__instance, card);
        }
    }
}
