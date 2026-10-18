package patches;

import cards.scholar.PowerOfOriginalSin;
import cards.scholar.TwinPrincesTalisman;
import cards.scholar.KingdomLegacy;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;

public final class ScholarCardRenamePatch {
    // Resolve old save IDs without adding duplicate entries to random pools or the compendium.
    @SpirePatch(clz = CardLibrary.class, method = "getCard", paramtypez = {String.class})
    public static class LegacyLookup {
        @SpirePostfixPatch
        public static AbstractCard postfix(AbstractCard __result, String key) {
            if (__result != null) return __result;
            if ("OriginalSin".equals(key)) return CardLibrary.cards.get(PowerOfOriginalSin.ID);
            if ("PowerOfTwinPrinces".equals(key)) return CardLibrary.cards.get(TwinPrincesTalisman.ID);
            if ("RoyalLegacy".equals(key)) return CardLibrary.cards.get(KingdomLegacy.ID);
            return null;
        }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getCard",
            paramtypez = {AbstractPlayer.PlayerClass.class, String.class})
    public static class LegacyClassLookup {
        @SpirePostfixPatch
        public static AbstractCard postfix(AbstractCard __result, AbstractPlayer.PlayerClass plyrClass, String key) {
            return LegacyLookup.postfix(__result, key);
        }
    }
}
