package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import general.SmithingBody;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class OperationeSolisPotionPatch {
    @SpirePatch(clz = AbstractPotion.class, method = "addToBot")
    public static class PotionRoots {
        @SpireRawPatch
        public static void raw(CtBehavior method) throws Exception {
            for (String name : new String[] {"addToBot", "addToTop"}) {
                method.getDeclaringClass().getDeclaredMethod(name)
                        .insertBefore("{ general.PotionTaskHistory.mark($1); }");
            }
        }
    }

    @SpirePatch(clz = AbstractGameAction.class, method = "addToBot")
    public static class ChildActions {
        @SpireRawPatch
        public static void raw(CtBehavior method) throws Exception {
            for (String name : new String[] {"addToBot", "addToTop"}) {
                method.getDeclaringClass().getDeclaredMethod(name)
                        .insertBefore("{ general.PotionTaskHistory.inherit(this, $1); }");
            }
        }
    }

    @SpirePatch(clz = SmithingBody.class, method = "updateAction")
    public static class ActionScope {
        @SpireRawPatch
        public static void raw(CtBehavior method) throws Exception {
            method.insertBefore("{ general.PotionTaskHistory.begin($1); }");
            method.insertAfter("{ general.PotionTaskHistory.end(); }", true);
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage", paramtypez = {DamageInfo.class})
    public static class Damage {
        @SpireRawPatch
        public static void raw(CtBehavior method) throws Exception {
            method.addLocalVariable("ernSolarHealth", CtClass.intType);
            method.insertBefore("{ ernSolarHealth = this.currentHealth; }");
            method.insertAfter("{ if ($1.type != com.megacrit.cardcrawl.cards.DamageInfo.DamageType.HP_LOSS)"
                    + " general.PotionTaskHistory.record(1, ernSolarHealth - this.currentHealth);"
                    + " general.SolarCardBlockHistory.enemyDamaged($1, ernSolarHealth - this.currentHealth); }");
        }
    }

    @SpirePatch(clz = EnergyPanel.class, method = "addEnergy", paramtypez = {int.class})
    public static class Energy {
        @SpireRawPatch
        public static void raw(CtBehavior method) throws Exception {
            method.addLocalVariable("ernSolarEnergy", CtClass.intType);
            method.insertBefore("{ ernSolarEnergy = totalCount; }");
            method.insertAfter("{ general.PotionTaskHistory.record(2, totalCount - ernSolarEnergy);"
                    + " patches.OperationeSolisPatch.intervalEffect(2, totalCount - ernSolarEnergy); }");
        }
    }

    @SpirePatch(clz = EnergyManager.class, method = "recharge")
    public static class TurnRecharge {
        @SpirePostfixPatch
        public static void postfix(EnergyManager __instance) {
            // A normal recharge replaces unspent energy, rather than calling addEnergy.
            if (EnergyPanel.totalCount > 0) OperationeSolisPatch.intervalEffect(2, __instance.energy);
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "addBlock", paramtypez = {int.class})
    public static class Block {
        @SpireRawPatch
        public static void raw(CtBehavior method) throws Exception {
            method.addLocalVariable("ernSolarBlock", CtClass.intType);
            method.insertBefore("{ ernSolarBlock = this.currentBlock; }");
            method.insertAfter("{ if (this == com.megacrit.cardcrawl.dungeons.AbstractDungeon.player)"
                    + " { general.PotionTaskHistory.record(8, this.currentBlock - ernSolarBlock);"
                    + " patches.OperationeSolisPatch.blockGained(this.currentBlock - ernSolarBlock); } }");
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "draw", paramtypez = {int.class})
    public static class Draw {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall call) throws javassist.CannotCompileException {
                    if (call.getClassName().equals("com.megacrit.cardcrawl.cards.CardGroup")
                            && call.getMethodName().equals("addToHand")) {
                        call.replace("{ $proceed($$); general.PotionTaskHistory.record(4, 1);"
                                + " patches.OperationeSolisPatch.intervalEffect(4, 1); }");
                    }
                }
            };
        }
    }
}
