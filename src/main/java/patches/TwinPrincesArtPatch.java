package patches;

import cards.scholar.PowerOfTwinPrinces;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

public class TwinPrincesArtPatch {
    @SpirePatch(clz = SingleCardViewPopup.class, method = "canToggleBetaArt")
    public static class EnableToggle {
        @SpirePostfixPatch
        public static boolean postfix(boolean __result, SingleCardViewPopup __instance, AbstractCard ___card) {
            return __result || ___card instanceof PowerOfTwinPrinces;
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "render")
    public static class RefreshPreview {
        @SpirePrefixPatch
        public static void prefix(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card) {
            if (___card instanceof PowerOfTwinPrinces) {
                ((PowerOfTwinPrinces) ___card).refreshVariant();
            }
        }
    }
}
