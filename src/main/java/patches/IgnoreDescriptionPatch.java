package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public final class IgnoreDescriptionPatch {
    private IgnoreDescriptionPatch() {}

    public static String decorate(AbstractCard card, String description) {
        if (!IgnoreField.isIgnored(card) || description == null
                || DarkswordDescriptionPatch.shouldHideDescription(card)) return description;
        String prefix = CardCrawlGame.languagePack.getCardStrings("Ignore").EXTENDED_DESCRIPTION[1];
        return description.startsWith(prefix) ? description : prefix + description;
    }

    private static ExprEditor descriptionReader() {
        return new ExprEditor() {
            @Override
            public void edit(FieldAccess field) throws CannotCompileException {
                if (field.isReader() && "rawDescription".equals(field.getFieldName())
                        && AbstractCard.class.getName().equals(field.getClassName())) {
                    field.replace("$_ = patches.IgnoreDescriptionPatch.decorate($0, $proceed());");
                }
            }
        };
    }

    @SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
    public static class English {
        @SpireInstrumentPatch
        public static ExprEditor instrument() { return descriptionReader(); }
    }

    @SpirePatch(clz = AbstractCard.class, method = "initializeDescriptionCN")
    public static class Chinese {
        @SpireInstrumentPatch
        public static ExprEditor instrument() { return descriptionReader(); }
    }
}
