package patches;

import basemod.helpers.dynamicvariables.DamageVariable;
import cards.ironeye.GhostMillstone;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

public final class GhostMillstonePreviewPatch {
    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderDescription")
    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderDescriptionCN")
    public static class Popup {
        @SpirePrefixPatch
        public static void prefix(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card) {
            if (___card instanceof GhostMillstone) ___card.applyPowers();
        }
    }

    @SpirePatch(clz = DamageVariable.class, method = "baseValue")
    public static class EnglishDamage {
        @SpirePostfixPatch
        public static int postfix(int __result, DamageVariable __instance, AbstractCard card) {
            // BaseMod's English popup reads baseValue rather than the calculated value.
            return card instanceof GhostMillstone ? card.damage : __result;
        }
    }
}
