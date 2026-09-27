package patches;

import actions.InsuranceReturnAction;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public final class InsuranceField {
    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static final SpireField<Boolean> insured = new SpireField<>(() -> false);
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class CopyPatch {
        @SpirePostfixPatch
        public static AbstractCard postfix(AbstractCard __result, AbstractCard __instance) {
            inherit(__result, __instance);
            return __result;
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "moveToExhaustPile", paramtypez = {AbstractCard.class})
    public static class ExhaustPatch {
        @SpirePostfixPatch
        public static void postfix(CardGroup __instance, AbstractCard c) {
            if (isInsured(c) && AbstractDungeon.player != null && AbstractDungeon.actionManager != null
                    && AbstractDungeon.player.exhaustPile.contains(c)) {
                // Queue only after all exhaust hooks and the actual pile transfer have completed.
                AbstractDungeon.actionManager.addToBottom(new InsuranceReturnAction(AbstractDungeon.player, c));
            }
        }
    }

    private InsuranceField() {
    }

    public static boolean isInsured(AbstractCard card) {
        return card != null && Fields.insured.get(card);
    }

    public static void insure(AbstractCard card) {
        if (card != null) {
            Fields.insured.set(card, true);
            card.initializeDescription();
        }
    }

    public static void inherit(AbstractCard destination, AbstractCard source) {
        if (isInsured(source)) {
            insure(destination);
        }
    }
}
