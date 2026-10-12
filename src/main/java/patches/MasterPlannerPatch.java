package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DiscardAtEndOfTurnAction;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

@SpirePatch(clz = DiscardAtEndOfTurnAction.class, method = "update")
public class MasterPlannerPatch {
    @SpireInstrumentPatch
    public static ExprEditor instrument() {
        return new ExprEditor() {
            @Override
            public void edit(NewExpr expr) throws CannotCompileException {
                if (DiscardAction.class.getName().equals(expr.getClassName())) {
                    expr.replace("$_ = new actions.MasterPlannerDiscardAction($$);");
                }
            }
        };
    }
}
