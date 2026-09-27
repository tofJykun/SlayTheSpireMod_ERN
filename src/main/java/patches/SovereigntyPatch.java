package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.DiscardAtEndOfTurnAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import powers.SovereigntyPower;

@SpirePatch(clz = DiscardAtEndOfTurnAction.class, method = "update")
public class SovereigntyPatch {
    @SpireInstrumentPatch
    public static ExprEditor instrument() {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall call) throws CannotCompileException {
                if (call.getClassName().equals(AbstractPlayer.class.getName())
                        && call.getMethodName().equals("hasRelic")) {
                    // Only extend the end-turn Pyramid gate; retain and end-turn hooks stay vanilla.
                    call.replace("$_ = $proceed($$) || (\"Runic Pyramid\".equals($1) && $0.hasPower(\""
                            + SovereigntyPower.POWER_ID + "\"));");
                }
            }
        };
    }
}
