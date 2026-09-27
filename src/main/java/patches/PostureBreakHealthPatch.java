package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

@SpirePatch(clz = AbstractPlayer.class, method = "damage", paramtypez = {DamageInfo.class})
public class PostureBreakHealthPatch {
    @SpireInstrumentPatch
    public static ExprEditor instrument() {
        return new ExprEditor() {
            @Override
            public void edit(FieldAccess field) throws CannotCompileException {
                if (field.isWriter() && field.getFieldName().equals("currentHealth")) {
                    // Observe actual HP writes after all damage prevention, before
                    // a revive/heal can conceal an earlier loss within the same hit.
                    field.replace("{ powers.PostureBreakPower.recordHealthWrite($0, info, $1); $proceed($$); }");
                }
            }
        };
    }
}
