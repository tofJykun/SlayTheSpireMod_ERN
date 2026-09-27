package patches;

import actions.RandomPlayHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import powers.AllKnowingHelmPower;

public class AllKnowingHelmPatch {
    @SpirePatch(clz = AbstractCard.class, method = "freeToPlay")
    public static class FreeToPlay {
        @SpirePostfixPatch
        public static boolean postfix(boolean __result, AbstractCard __instance) {
            return __result || AllKnowingHelmPower.shouldMakeFree(__instance);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "getCost")
    public static class DisplayCost {
        @SpirePostfixPatch
        public static String postfix(String __result, AbstractCard __instance) {
            return AllKnowingHelmPower.shouldMakeFree(__instance) ? "0" : __result;
        }
    }

    public static AbstractCard.CardTarget inputTarget(AbstractCard card) {
        return AllKnowingHelmPower.shouldRandomize(card)
                && !RandomPlayHelper.shouldChooseRandomPlay(AbstractDungeon.player)
                ? AbstractCard.CardTarget.NONE : card.target;
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "clickAndDragCards")
    public static class TargetInput {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess field) throws CannotCompileException {
                    if (field.isReader() && field.getClassName().equals(AbstractCard.class.getName())
                            && field.getFieldName().equals("target")) {
                        // Only change aiming UI, never the card's real target or autoplay behavior.
                        field.replace("{ $_ = patches.AllKnowingHelmPatch.inputTarget($0); }");
                    }
                }
            };
        }
    }
}
