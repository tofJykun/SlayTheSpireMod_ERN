package patches;

import cards.ironeye.CobCannon;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

public class CobCannonRefreshPatch {
    @SpirePatch(clz = CardGroup.class, method = "addToHand", paramtypez = { AbstractCard.class })
    public static class AddToHandPatch {
        @SpirePostfixPatch
        public static void postfix(CardGroup __instance, AbstractCard c) {
            CobCannon.refreshAllCobCannons();
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "addToTop", paramtypez = { AbstractCard.class })
    public static class AddToTopPatch {
        @SpirePostfixPatch
        public static void postfix(CardGroup __instance, AbstractCard c) {
            CobCannon.refreshAllCobCannons();
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "addToBottom", paramtypez = { AbstractCard.class })
    public static class AddToBottomPatch {
        @SpirePostfixPatch
        public static void postfix(CardGroup __instance, AbstractCard c) {
            CobCannon.refreshAllCobCannons();
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "addToRandomSpot", paramtypez = { AbstractCard.class })
    public static class AddToRandomSpotPatch {
        @SpirePostfixPatch
        public static void postfix(CardGroup __instance, AbstractCard c) {
            CobCannon.refreshAllCobCannons();
        }
    }
}
