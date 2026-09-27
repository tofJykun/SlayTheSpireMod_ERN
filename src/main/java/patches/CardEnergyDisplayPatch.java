package patches;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.ListIterator;

public final class CardEnergyDisplayPatch {
    private CardEnergyDisplayPatch() {}

    private static boolean isModCard(AbstractCard card) {
        return card != null && card.getClass().getName().startsWith("cards.");
    }

    public static boolean isEnergyKeyword(String keyword) {
        return keyword != null && (keyword.matches("\\[[ERGBWergbw]\\]")
                || keyword.equalsIgnoreCase(TipHelper.TEXT[0]));
    }

    // BaseMod adds [E] while vanilla can also add a color-specific energy keyword.
    public static void normalize(AbstractCard card) {
        if (!isModCard(card)) {
            return;
        }
        boolean found = false;
        ListIterator<String> iterator = card.keywords.listIterator();
        while (iterator.hasNext()) {
            if (isEnergyKeyword(iterator.next())) {
                if (found) {
                    iterator.remove();
                } else {
                    iterator.set("[E]");
                    found = true;
                }
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
    public static class English {
        @SpirePostfixPatch
        public static void postfix(AbstractCard __instance) { normalize(__instance); }
    }

    @SpirePatch(clz = AbstractCard.class, method = "initializeDescriptionCN")
    public static class Chinese {
        @SpirePostfixPatch
        public static void postfix(AbstractCard __instance) { normalize(__instance); }
    }

    @SpirePatch(clz = BaseMod.class, method = "getCardSmallEnergy", paramtypez = {AbstractCard.class})
    public static class CardIcon {
        @SpirePostfixPatch
        public static TextureAtlas.AtlasRegion postfix(TextureAtlas.AtlasRegion __result, AbstractCard card) {
            return isModCard(card) && AbstractDungeon.player != null
                    ? AbstractDungeon.player.getOrb() : __result;
        }
    }

    public static void addPopupTip(AbstractCard card, ArrayList<PowerTip> tips) {
        if (!isModCard(card) || card.isLocked || !card.isSeen) {
            return;
        }
        normalize(card);
        if (!card.keywords.contains("[E]")) {
            return;
        }
        tips.removeIf(tip -> isEnergyKeyword(tip.header));
        tips.add(new PowerTip(TipHelper.capitalize(TipHelper.TEXT[0]),
                GameDictionary.keywords.get("[E]"), BaseMod.getCardSmallEnergy(card)));
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderTips")
    public static class Popup {
        // Vanilla (and BaseMod's FixEnergyTooltip) skip energy tips in the large view.
        @SpireInsertPatch(locator = BeforeEmptyCheck.class, localvars = {"t"})
        public static void insert(SingleCardViewPopup __instance, SpriteBatch sb,
                                  AbstractCard ___card, ArrayList<PowerTip> t) {
            addPopupTip(___card, t);
        }
    }

    public static class BeforeEmptyCheck extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior method) throws Exception {
            return LineFinder.findInOrder(method, new Matcher.MethodCallMatcher(ArrayList.class, "isEmpty"));
        }
    }
}
