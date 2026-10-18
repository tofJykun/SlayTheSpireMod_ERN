package patches;

import basemod.helpers.dynamicvariables.DamageVariable;
import cards.scholar.FirePot;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

public final class FirePotPreviewPatch {
    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderDescription")
    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderDescriptionCN")
    public static class Popup {
        @SpirePrefixPatch public static void prefix(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card) {
            if (___card instanceof FirePot) ___card.applyPowers();
        }
    }

    @SpirePatch(clz = DamageVariable.class, method = "baseValue")
    public static class Damage {
        @SpirePostfixPatch public static int postfix(int __result, DamageVariable __instance, AbstractCard card) {
            return card instanceof FirePot ? card.damage : __result;
        }
    }
}
