package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public final class ShadowGuardDescriptionPatch {
    private ShadowGuardDescriptionPatch() {}

    public static String decorate(AbstractCard card, String description) {
        if (!ShadowGuardField.isMarked(card) || description == null
                || DarkswordDescriptionPatch.shouldHideDescription(card)) return description;
        String suffix = CardCrawlGame.languagePack.getCardStrings("ShadowGuard").EXTENDED_DESCRIPTION[1];
        return description.endsWith(suffix) ? description : description + suffix;
    }

    private static ExprEditor descriptionReader() {
        return new ExprEditor() {
            @Override
            public void edit(FieldAccess field) throws CannotCompileException {
                if (field.isReader() && "rawDescription".equals(field.getFieldName())
                        && AbstractCard.class.getName().equals(field.getClassName())) {
                    field.replace("$_ = patches.ShadowGuardDescriptionPatch.decorate($0, $proceed());");
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
