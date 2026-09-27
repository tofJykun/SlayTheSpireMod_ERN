package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import general.SmithingBody;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.FieldAccess;

import java.util.HashSet;
import java.util.UUID;

public class SmithingBodyPatch {
    @SpirePatch(clz = com.megacrit.cardcrawl.actions.defect.GashAction.class, method = "update")
    public static class ClawInstances {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override public void edit(FieldAccess field) throws CannotCompileException {
                    if (field.isReader() && field.getClassName().equals(CardGroup.class.getName())
                            && field.getFieldName().equals("group")) {
                        field.replace("{ $_ = general.SmithingBody.behaviorCards($0); }");
                    }
                }
            };
        }
    }
    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class Copy {
        @SpirePostfixPatch
        public static AbstractCard postfix(AbstractCard __result, AbstractCard __instance) {
            SmithingBody.copied(__instance, __result);
            return __result;
        }
    }

    @SpirePatch(clz = GetAllInBattleInstances.class, method = "get", paramtypez = {UUID.class})
    public static class Instances {
        @SpirePrefixPatch
        public static SpireReturn<HashSet<AbstractCard>> prefix(UUID uuid) {
            return SpireReturn.Return(SmithingBody.instances(uuid));
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "update")
    public static class ActionContext {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override public void edit(MethodCall call) throws CannotCompileException {
                    if (call.getClassName().equals("com.megacrit.cardcrawl.actions.AbstractGameAction")
                            && call.getMethodName().equals("update")) {
                        call.replace("{ general.SmithingBody.updateAction($0); }");
                    }
                }
            };
        }

        @SpireRawPatch
        public static void raw(CtBehavior method) throws Exception {
            CtClass manager = method.getDeclaringClass();
            for (String name : new String[] {"addToBottom", "addToTop"}) {
                manager.getDeclaredMethod(name).insertBefore("{ general.SmithingBody.queued($1); }");
            }
            manager.getDeclaredMethod("clear").insertAfter("{ general.SmithingBody.clear(); }");
            // Constructors are selected by their bytecode signature, never by debug parameter names.
            ClassPool pool = manager.getClassPool();
            for (String name : new String[] {
                    "com.megacrit.cardcrawl.cards.CardQueueItem",
                    "com.megacrit.cardcrawl.actions.utility.DiscardToHandAction",
                    "com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction",
                    "com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction",
                    "com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction",
                    "com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction",
                    "com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAndDeckAction",
                    "com.megacrit.cardcrawl.actions.common.MakeTempCardAtBottomOfDeckAction"}) {
                for (CtConstructor constructor : pool.get(name).getDeclaredConstructors()) {
                    String code = normalizeParameters(constructor);
                    if (name.equals(CardQueueItem.class.getName())) code = code.replace(".physical(", ".forQueue(");
                    if (!code.isEmpty()) constructor.insertBeforeBody("{" + code + "}");
                }
            }
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "contains", paramtypez = {AbstractCard.class})
    public static class PileIdentity {
        @SpireRawPatch
        public static void raw(CtBehavior method) throws Exception {
            for (CtMethod member : method.getDeclaringClass().getDeclaredMethods()) {
                if (Modifier.isStatic(member.getModifiers())) continue;
                String code = normalizeParameters(member);
                if (!code.isEmpty()) member.insertBefore("{" + code + "}");
            }
        }
    }

    private static String normalizeParameters(CtBehavior method) throws NotFoundException {
        StringBuilder code = new StringBuilder();
        CtClass[] params = method.getParameterTypes();
        for (int i = 0; i < params.length; i++) {
            if (params[i].getName().equals(AbstractCard.class.getName())) {
                code.append('$').append(i + 1).append(" = general.SmithingBody.physical($")
                        .append(i + 1).append(");");
            }
        }
        return code.toString();
    }
}
