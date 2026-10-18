package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public final class SowingField {
    private SowingField() { }

    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static final SpireField<Boolean> sown = new SpireField<>(() -> false);
    }

    public static boolean isSown(AbstractCard card) { return card != null && Fields.sown.get(card); }

    public static void sow(AbstractCard card) {
        if (card == null || isSown(card)) return;
        Fields.sown.set(card, true);
        card.initializeDescription();
    }

    public static void inherit(AbstractCard destination, AbstractCard source) {
        if (isSown(source)) sow(destination);
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class Copy {
        @SpirePostfixPatch
        public static AbstractCard postfix(AbstractCard __result, AbstractCard __instance) {
            inherit(__result, __instance);
            return __result;
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class Play {
        @SpirePostfixPatch
        public static void postfix(AbstractPlayer __instance, AbstractCard card) {
            if (isSown(card) && !card.dontTriggerOnUseCard) {
                AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
            }
        }
    }
}
