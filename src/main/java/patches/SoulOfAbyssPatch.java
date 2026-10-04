package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import general.CombatState;
import powers.SoulOfAbyssPower;

public class SoulOfAbyssPatch {
    public static void created(AbstractCard card) {
        if (card == null || card.type != AbstractCard.CardType.STATUS
                || AbstractDungeon.player == null || !CombatState.isInCombat()
                || !general.StatusGenerationSource.isPlayerSource()) {
            return;
        }
        AbstractPower power = AbstractDungeon.player.getPower(SoulOfAbyssPower.POWER_ID);
        if (power instanceof SoulOfAbyssPower) {
            ((SoulOfAbyssPower)power).onStatusCreated();
        }
    }

    // Only the constructors that actually add a card, not delegating overloads.
    @SpirePatch(clz = ShowCardAndAddToHandEffect.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCard.class})
    public static class Hand {
        @SpirePostfixPatch
        public static void postfix(ShowCardAndAddToHandEffect __instance, AbstractCard card) { created(card); }
    }

    @SpirePatch(clz = ShowCardAndAddToHandEffect.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCard.class, float.class, float.class})
    public static class HandPositioned {
        @SpirePostfixPatch
        public static void postfix(ShowCardAndAddToHandEffect __instance, AbstractCard card) { created(card); }
    }

    @SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCard.class})
    public static class Discard {
        @SpirePostfixPatch
        public static void postfix(ShowCardAndAddToDiscardEffect __instance, AbstractCard card) { created(card); }
    }

    @SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCard.class, float.class, float.class})
    public static class DiscardPositioned {
        @SpirePostfixPatch
        public static void postfix(ShowCardAndAddToDiscardEffect __instance, AbstractCard card) { created(card); }
    }

    @SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCard.class, boolean.class, boolean.class})
    public static class DrawPile {
        @SpirePostfixPatch
        public static void postfix(ShowCardAndAddToDrawPileEffect __instance, AbstractCard card) { created(card); }
    }

    @SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCard.class, float.class, float.class, boolean.class, boolean.class, boolean.class})
    public static class DrawPilePositioned {
        @SpirePostfixPatch
        public static void postfix(ShowCardAndAddToDrawPileEffect __instance, AbstractCard card) { created(card); }
    }
}
