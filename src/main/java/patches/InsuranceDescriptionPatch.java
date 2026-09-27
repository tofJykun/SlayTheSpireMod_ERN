package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public final class InsuranceDescriptionPatch {
    private InsuranceDescriptionPatch() {
    }

    public static String decorate(AbstractCard card, String description) {
        if (!InsuranceField.isInsured(card) || description == null
                || DarkswordDescriptionPatch.shouldHideDescription(card)) {
            return description;
        }
        String prefix = CardCrawlGame.languagePack.getCardStrings("Insurance").EXTENDED_DESCRIPTION[1];
        return description.startsWith(prefix) ? description : prefix + description;
    }

    // Decorate the parser input, not rawDescription: dynamic cards and other description patches keep their source text.
    private static ExprEditor descriptionReader() {
        return new ExprEditor() {
            @Override
            public void edit(FieldAccess field) throws CannotCompileException {
                if (field.isReader() && "rawDescription".equals(field.getFieldName())
                        && AbstractCard.class.getName().equals(field.getClassName())) {
                    field.replace("$_ = patches.InsuranceDescriptionPatch.decorate($0, $proceed());");
                }
            }
        };
    }

    @SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
    public static class English {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return descriptionReader();
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "initializeDescriptionCN")
    public static class Chinese {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return descriptionReader();
        }
    }
}
