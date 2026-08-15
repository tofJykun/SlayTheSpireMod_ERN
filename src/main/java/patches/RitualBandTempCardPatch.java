package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import relics.RitualBand;

public class RitualBandTempCardPatch {
    @SpirePatch(clz = MakeTempCardInHandAction.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCard.class, boolean.class})
    public static class MakeTempCardInHandBoolean {
        @SpirePrefixPatch
        public static void Prefix(MakeTempCardInHandAction __instance, AbstractCard card) {
            RitualBand.upgradeFadingPrimalGlintstone(card);
        }
    }

    @SpirePatch(clz = MakeTempCardInHandAction.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCard.class, int.class})
    public static class MakeTempCardInHandAmount {
        @SpirePrefixPatch
        public static void Prefix(MakeTempCardInHandAction __instance, AbstractCard card) {
            RitualBand.upgradeFadingPrimalGlintstone(card);
        }
    }

    @SpirePatch(clz = MakeTempCardInDiscardAction.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCard.class, int.class})
    public static class MakeTempCardInDiscardAmount {
        @SpirePrefixPatch
        public static void Prefix(MakeTempCardInDiscardAction __instance, AbstractCard card) {
            RitualBand.upgradeFadingPrimalGlintstone(card);
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCard.class, float.class, float.class, boolean.class, boolean.class, boolean.class})
    public static class ShowCardAndAddToDrawPileFull {
        @SpirePrefixPatch
        public static void Prefix(ShowCardAndAddToDrawPileEffect __instance, AbstractCard srcCard) {
            RitualBand.upgradeFadingPrimalGlintstone(srcCard);
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCard.class, boolean.class, boolean.class})
    public static class ShowCardAndAddToDrawPileSimple {
        @SpirePrefixPatch
        public static void Prefix(ShowCardAndAddToDrawPileEffect __instance, AbstractCard srcCard) {
            RitualBand.upgradeFadingPrimalGlintstone(srcCard);
        }
    }
}
