package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.random.Random;
import general.CommunionCombat;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class CommunionPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class CardScope {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall call) throws javassist.CannotCompileException {
                    if (call.getMethodName().equals("use")
                            && call.getClassName().equals("com.megacrit.cardcrawl.cards.AbstractCard")) {
                        call.replace("{ general.CommunionCombat.beginCard($0);"
                                + " try { $proceed($$); } finally { general.CommunionCombat.endCard(); } }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "addToBottom")
    @SpirePatch(clz = GameActionManager.class, method = "addToTop")
    public static class Queued {
        @SpirePrefixPatch
        public static void prefix(GameActionManager __instance, AbstractGameAction action) {
            CommunionCombat.queued(action);
        }
    }

    @SpirePatch(clz = general.SmithingBody.class, method = "updateAction")
    public static class ActionScope {
        @SpireRawPatch
        public static void raw(CtBehavior method) throws Exception {
            method.insertBefore("{ general.CommunionCombat.beginAction($1); }");
            method.insertAfter("{ general.CommunionCombat.endAction($1); }", true);
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep")
    public static class Reset {
        @SpirePrefixPatch
        public static void prefix() { CommunionCombat.reset(); }
    }

    @SpirePatch(clz = DamageInfo.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCreature.class, int.class, DamageInfo.DamageType.class})
    public static class DamageCreated {
        @SpirePostfixPatch
        public static void postfix(DamageInfo __instance) { CommunionCombat.created(__instance); }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class Damage {
        @SpirePrefixPatch
        public static void prefix() { CommunionCombat.enterDamage(); }

        @SpirePostfixPatch
        public static void postfix(AbstractMonster __instance, DamageInfo info) {
            CommunionCombat.exitDamage(__instance, info);
        }
    }

    @SpirePatch(clz = AbstractGameAction.class, method = "shouldCancelAction")
    public static class ContinueMultiHit {
        @SpirePostfixPatch
        public static boolean postfix(boolean __result, AbstractGameAction __instance) {
            return __result && !CommunionCombat.keepDamageAction(__instance);
        }
    }

    @SpirePatch(clz = MonsterGroup.class, method = "getRandomMonster",
            paramtypez = {AbstractMonster.class, boolean.class, Random.class})
    @SpirePatch(clz = MonsterGroup.class, method = "getRandomMonster",
            paramtypez = {AbstractMonster.class, boolean.class})
    public static class RandomTarget {
        @SpirePrefixPatch
        public static SpireReturn<AbstractMonster> prefix(MonsterGroup __instance, AbstractMonster exception) {
            return CommunionCombat.randomAttack()
                    ? SpireReturn.Return(CommunionCombat.firstTarget(__instance, exception)) : SpireReturn.Continue();
        }
    }
}
