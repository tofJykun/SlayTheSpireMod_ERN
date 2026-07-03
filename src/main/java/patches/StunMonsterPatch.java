package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import powers.DelayedStunPower;
import powers.StunPower;

public class StunMonsterPatch {
    @SpirePatch(clz = AbstractMonster.class, method = "rollMove")
    public static class RollMovePatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> prefix(AbstractMonster __instance) {
            if (__instance.hasPower(StunPower.POWER_ID)) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class TakeTurnPatch {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall methodCall) throws CannotCompileException {
                    if (methodCall.getClassName().equals(AbstractMonster.class.getName())
                            && methodCall.getMethodName().equals("takeTurn")) {
                        methodCall.replace("if (!((com.megacrit.cardcrawl.monsters.AbstractMonster)$0).hasPower(\""
                                + StunPower.POWER_ID + "\")) { $_ = $proceed($$); }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = ApplyPowerAction.class, method = "update")
    public static class ArtifactBypassPatch {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall methodCall) throws CannotCompileException {
                    if (methodCall.getMethodName().equals("hasPower")) {
                        methodCall.replace("$_ = ($1.equals(\"Artifact\") && this.powerToApply != null && "
                                + "(this.powerToApply.ID.equals(\"" + StunPower.POWER_ID + "\") || "
                                + "this.powerToApply.ID.equals(\"" + DelayedStunPower.POWER_ID + "\"))) "
                                + "? false : $proceed($$);");
                    }
                }
            };
        }
    }
}
