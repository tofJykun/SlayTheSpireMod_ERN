package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.SolarAttackHistory;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class OperationeSolisAttackPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class CardScope {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall call) throws javassist.CannotCompileException {
                    if (call.getMethodName().equals("use")
                            && call.getClassName().equals("com.megacrit.cardcrawl.cards.AbstractCard")) {
                        call.replace("{ general.SolarAttackHistory.beginCard($0);"
                                + " try { $proceed($$); } finally { general.SolarAttackHistory.endCard(); } }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "addToBottom")
    public static class QueuedActions {
        @SpireRawPatch
        public static void raw(CtBehavior method) throws Exception {
            for (String name : new String[] {"addToBottom", "addToTop"}) {
                method.getDeclaringClass().getDeclaredMethod(name)
                        .insertBefore("{ general.SolarAttackHistory.queued($1); }");
            }
        }
    }

    @SpirePatch(clz = general.SmithingBody.class, method = "updateAction")
    public static class ActionScope {
        @SpireRawPatch
        public static void raw(CtBehavior method) throws Exception {
            method.insertBefore("{ general.SolarAttackHistory.beginAction($1); }");
            method.insertAfter("{ general.SolarAttackHistory.endAction($1); }", true);
        }
    }

    @SpirePatch(clz = DamageInfo.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {com.megacrit.cardcrawl.core.AbstractCreature.class, int.class, DamageInfo.DamageType.class})
    public static class DamageSource {
        @SpirePostfixPatch
        public static void postfix(DamageInfo __instance) { SolarAttackHistory.created(__instance); }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage", paramtypez = {DamageInfo.class})
    public static class OverkillDamage {
        @SpireInsertPatch(locator = OverkillLocator.class, localvars = {"damageAmount"})
        public static void insert(AbstractMonster __instance, DamageInfo info, int damageAmount) {
            SolarAttackHistory.record(info, damageAmount);
        }
    }

    public static class OverkillLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior method) throws Exception {
            return LineFinder.findInOrder(method, new Matcher.FieldAccessMatcher(CardCrawlGame.class, "overkill"));
        }
    }
}
