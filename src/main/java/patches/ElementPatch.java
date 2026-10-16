package patches;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import general.CardElement;
import general.ElementHelper;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.FieldAccess;

public final class ElementPatch {
    @SpirePatch(clz = AbstractCard.class, method = "renderDescriptionCN")
    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderDescriptionCN")
    public static class ChineseKeywordRendering {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall call) throws CannotCompileException {
                    if (call.getClassName().equals(Character.class.getName())
                            && call.getMethodName().equals("isLetter")) {
                        // CN tokens have no trailing space: the native punctuation check eats ":fire".
                        call.replace("{ $_ = general.CardElement.isChineseElementKeyword(tmp) || $proceed($$); }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
    public static class EnglishKeywords {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess field) throws CannotCompileException {
                    if (field.isReader() && field.getClassName().equals(AbstractCard.class.getName())
                            && field.getFieldName().equals("rawDescription")) {
                        // BaseMod needs one underscore-joined token for a multiword keyword.
                        field.replace("{ $_ = general.CardElement.descriptionTokens($proceed()); }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static final SpireField<CardElement> element = new SpireField<>(() -> null);
        public static final SpireField<Boolean> elementCopy = new SpireField<>(() -> false);
        public static final SpireField<Boolean> surcharge = new SpireField<>(() -> false);
        public static final SpireField<Boolean> originalModified = new SpireField<>(() -> false);
        public static final SpireField<Integer> displayedCost = new SpireField<>(() -> 0);
        public static final SpireField<Boolean> resolving = new SpireField<>(() -> false);
        public static final SpireField<AbstractCard> pendingCopy = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class Copy {
        @SpirePostfixPatch
        public static AbstractCard postfix(AbstractCard __result, AbstractCard __instance) {
            ElementHelper.inherit(__result, __instance);
            return __result;
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "update")
    @SpirePatch(clz = AbstractCard.class, method = "hasEnoughEnergy")
    @SpirePatch(clz = AbstractCard.class, method = "getCost")
    public static class Refresh {
        @SpirePrefixPatch
        public static void prefix(AbstractCard __instance) { ElementHelper.refreshCost(__instance); }
    }

    @SpirePatch(clz = AbstractCard.class, method = "updateCost")
    @SpirePatch(clz = AbstractCard.class, method = "setCostForTurn")
    @SpirePatch(clz = AbstractCard.class, method = "modifyCostForCombat")
    @SpirePatch(clz = AbstractCard.class, method = "resetAttributes")
    public static class CostChanges {
        @SpirePrefixPatch
        public static void prefix(AbstractCard __instance) { ElementHelper.removeSurcharge(__instance); }
        @SpirePostfixPatch
        public static void postfix(AbstractCard __instance) { ElementHelper.refreshCost(__instance); }
    }

    @SpirePatch(clz = AbstractCard.class, method = "triggerOnGlowCheck")
    public static class Glow {
        @SpirePostfixPatch
        public static void postfix(AbstractCard __instance) {
            if (ElementHelper.element(__instance) == null) return;
            int relation = ElementHelper.relation(__instance);
            __instance.glowColor = relation > 0 ? Color.GOLD.cpy() : relation < 0 ? Color.RED.cpy()
                    : new Color(0.2F, 0.9F, 1.0F, 0.25F);
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "useCard",
            paramtypez = {AbstractCard.class, AbstractMonster.class, int.class})
    public static class Play {
        @SpirePrefixPatch
        public static void prefix(AbstractPlayer __instance, AbstractCard card, AbstractMonster monster, int energy) {
            ElementHelper.beginUse(card);
        }
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall call) throws CannotCompileException {
                    if (!call.getClassName().equals(AbstractCard.class.getName())) return;
                    if (call.getMethodName().equals("use")) {
                        call.replace("{ general.ElementHelper.beforeEffect($0); $proceed($$); }");
                    } else if (call.getMethodName().equals("calculateCardDamage")) {
                        call.replace("{ if (!general.ElementHelper.isElementCopy($0)) $proceed($$); }");
                    }
                }
            };
        }
        @SpirePostfixPatch
        public static void postfix(AbstractPlayer __instance, AbstractCard card, AbstractMonster monster, int energy) {
            ElementHelper.finishUse(card, monster);
        }
    }
}
