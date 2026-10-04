package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class StatusGenerationSourcePatch {
    public static final String[] DISPATCHERS = {
        "com.megacrit.cardcrawl.characters.AbstractPlayer",
        "com.megacrit.cardcrawl.core.AbstractCreature",
        "com.megacrit.cardcrawl.monsters.AbstractMonster",
        "com.megacrit.cardcrawl.monsters.MonsterGroup",
        "com.megacrit.cardcrawl.actions.GameActionManager",
        "com.megacrit.cardcrawl.cards.CardGroup",
        "com.megacrit.cardcrawl.actions.utility.UseCardAction",
        "com.megacrit.cardcrawl.actions.common.DamageAction",
        "com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction",
        "com.megacrit.cardcrawl.actions.common.ApplyPowerAction",
        "com.megacrit.cardcrawl.actions.common.ReducePowerAction",
        "com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction",
        "com.megacrit.cardcrawl.ui.panels.PotionPopUp",
        "com.megacrit.cardcrawl.rooms.AbstractRoom"
    };

    @SpirePatch(clz = GameActionManager.class, method = "addToBottom")
    public static class Sources {
        @SpireRawPatch
        public static void raw(CtBehavior target) throws Exception {
            ClassPool pool = target.getDeclaringClass().getClassPool();
            // An enemy Hex lives on the player; ownership alone does not identify its producer.
            pool.get("com.megacrit.cardcrawl.actions.common.ApplyPowerAction").getDeclaredConstructor(new CtClass[] {
                pool.get("com.megacrit.cardcrawl.core.AbstractCreature"),
                pool.get("com.megacrit.cardcrawl.core.AbstractCreature"),
                pool.get("com.megacrit.cardcrawl.powers.AbstractPower"), CtClass.intType, CtClass.booleanType,
                pool.get("com.megacrit.cardcrawl.actions.AbstractGameAction$AttackEffect")
            }).insertAfter("{ general.StatusGenerationSource.powerCreated($3, $2); }");
            final CtClass[] origins = {
                pool.get("com.megacrit.cardcrawl.cards.AbstractCard"),
                pool.get("com.megacrit.cardcrawl.relics.AbstractRelic"),
                pool.get("com.megacrit.cardcrawl.potions.AbstractPotion"),
                pool.get("com.megacrit.cardcrawl.powers.AbstractPower"),
                pool.get("com.megacrit.cardcrawl.monsters.AbstractMonster")
            };
            // Scope callbacks themselves: an enemy Hex reaction must override a player card,
            // while a player relic reacting during an enemy action must remain player-owned.
            for (String name : DISPATCHERS) {
                for (CtMethod method : pool.get(name).getDeclaredMethods()) {
                    if (Modifier.isAbstract(method.getModifiers()) || Modifier.isNative(method.getModifiers())) continue;
                    method.instrument(new ExprEditor() {
                        @Override public void edit(MethodCall call) throws CannotCompileException {
                            try {
                                if (Modifier.isStatic(call.getMethod().getModifiers())) return;
                                CtClass type = call.getMethod().getDeclaringClass();
                                for (CtClass origin : origins) {
                                    if (type.subtypeOf(origin)) {
                                        call.replace("{ general.StatusGenerationSource.begin($0);"
                                                + " try { $_ = $proceed($$); } finally { general.StatusGenerationSource.end(); } }");
                                        return;
                                    }
                                }
                            } catch (NotFoundException e) { throw new CannotCompileException(e); }
                        }
                    });
                }
            }
            // Helpers are also used by mod powers/relics outside the native dispatch sites.
            for (CtClass origin : origins) {
                for (CtMethod method : origin.getDeclaredMethods()) {
                    if (method.getName().equals("addToBot") || method.getName().equals("addToTop")) {
                        method.insertBefore("{ general.StatusGenerationSource.begin(this); }");
                        method.insertAfter("{ general.StatusGenerationSource.end(); }", true);
                    }
                }
            }
            for (String name : new String[] {"addToBottom", "addToTop"}) {
                target.getDeclaringClass().getDeclaredMethod(name)
                        .insertBefore("{ general.StatusGenerationSource.queued($1); }");
            }
            CtMethod update = pool.get("general.SmithingBody").getDeclaredMethod("updateAction");
            update.insertBefore("{ general.StatusGenerationSource.beginAction($1); }");
            update.insertAfter("{ general.StatusGenerationSource.endAction($1); }", true);
            pool.get("com.megacrit.cardcrawl.characters.AbstractPlayer").getDeclaredMethod("preBattlePrep")
                    .insertBefore("{ general.StatusGenerationSource.reset(); }");
        }
    }
}
