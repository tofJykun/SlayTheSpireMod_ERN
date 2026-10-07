package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.screens.DrawPileViewScreen;
import com.megacrit.cardcrawl.ui.panels.DrawPilePanel;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import powers.TelescopePower;

public class TelescopePatch {
    public static ExprEditor frozenEyeOrTelescope() {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall call) throws CannotCompileException {
                if (call.getClassName().equals(AbstractPlayer.class.getName())
                        && call.getMethodName().equals("hasRelic")) {
                    // Extend only draw-pile UI gates, not ownership of the actual relic.
                    call.replace("$_ = $proceed($$) || (\"Frozen Eye\".equals($1) && $0.hasPower(\""
                            + TelescopePower.POWER_ID + "\"));");
                }
            }
        };
    }

    @SpirePatch(clz = DrawPileViewScreen.class, method = "open")
    public static class OrderedPreview {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return frozenEyeOrTelescope();
        }
    }

    @SpirePatch(clz = DrawPileViewScreen.class, method = "render")
    public static class PreviewTip {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return frozenEyeOrTelescope();
        }
    }

    @SpirePatch(clz = DrawPilePanel.class, method = "render")
    public static class PanelTip {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return frozenEyeOrTelescope();
        }
    }
}
