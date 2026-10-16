package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public final class LiteralBossTextPatch {
    public static char renderCharAt(String text, int index) {
        char value = text.charAt(index);
        // Only change token classification; the original text is still passed to the font renderer.
        return value == 'B' && text.startsWith("Boss", index) ? 'b' : value;
    }

    @SpirePatch(clz = AbstractCard.class, method = "renderDescriptionCN")
    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderDescriptionCN")
    public static class Render {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall call) throws CannotCompileException {
                    if (call.getClassName().equals(String.class.getName()) && call.getMethodName().equals("charAt")) {
                        call.replace("{ $_ = patches.LiteralBossTextPatch.renderCharAt($0, $1); }");
                    }
                }
            };
        }
    }
}
