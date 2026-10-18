package patches;

import basemod.helpers.dynamicvariables.BlockVariable;
import cards.scholar.AldiaShield;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

public final class AldiaShieldPreviewPatch {
    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderDescription")
    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderDescriptionCN")
    public static class Popup {
        @SpirePrefixPatch public static void prefix(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card) {
            if (___card instanceof AldiaShield) ___card.applyPowers();
        }
    }

    @SpirePatch(clz = BlockVariable.class, method = "baseValue")
    public static class Block {
        @SpirePostfixPatch public static int postfix(int __result, BlockVariable __instance, AbstractCard card) {
            return card instanceof AldiaShield ? card.block : __result;
        }
    }
}
