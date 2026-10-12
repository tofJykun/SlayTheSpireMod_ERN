package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import java.util.regex.Pattern;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public final class ReplayDescriptionPatch {
    private static final Pattern COUNT = Pattern.compile("(重放|Replay)(\\s+)(?:[0-9]+|![A-Za-z][A-Za-z0-9]*!)");

    public static String decorate(AbstractCard card, String description) {
        if (description == null) return null;
        String result = COUNT.matcher(description).replaceAll("$1$2!ERNReplay!");
        // Forged cards describe their components by name instead of listing Replay.
        if ("CraftmanCreation".equals(card.cardID) && ReplayField.getReplay(card) > 0
                && !result.contains("!ERNReplay!") && !DarkswordDescriptionPatch.shouldHideDescription(card)) {
            result += Settings.language == Settings.GameLanguage.ZHS
                    ? " NL 重放 !ERNReplay! 。" : " NL Replay !ERNReplay!.";
        }
        return result;
    }

    private static ExprEditor reader() {
        return new ExprEditor() {
            @Override
            public void edit(FieldAccess field) throws CannotCompileException {
                if (field.isReader() && "rawDescription".equals(field.getFieldName())
                        && AbstractCard.class.getName().equals(field.getClassName())) {
                    field.replace("$_ = patches.ReplayDescriptionPatch.decorate($0, $proceed());");
                }
            }
        };
    }

    @SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
    public static class English {
        @SpireInstrumentPatch
        public static ExprEditor instrument() { return reader(); }
    }

    @SpirePatch(clz = AbstractCard.class, method = "initializeDescriptionCN")
    public static class Chinese {
        @SpireInstrumentPatch
        public static ExprEditor instrument() { return reader(); }
    }
}
