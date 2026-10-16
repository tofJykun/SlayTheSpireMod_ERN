package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import general.BrandNewWorldProgress;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public final class BrandNewWorldPatch {
    @SpirePatch(clz = AbstractDungeon.class, method = "generateSeeds")
    public static class NewRun {
        @SpirePrefixPatch
        public static void prefix() { BrandNewWorldProgress.resetRun(); }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getAnyColorCard",
            paramtypez = {AbstractCard.CardType.class, AbstractCard.CardRarity.class})
    @SpirePatch(clz = CardLibrary.class, method = "getAnyColorCard",
            paramtypez = {AbstractCard.CardRarity.class})
    @SpirePatch(clz = CardLibrary.class, method = "getEachRare")
    @SpirePatch(clz = CardLibrary.class, method = "addCardsIntoPool")
    public static class LibraryCandidates {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess field) throws CannotCompileException {
                    if (field.isReader() && field.getClassName().equals(CardLibrary.class.getName())
                            && field.getFieldName().equals("cards")) {
                        field.replace("{ $_ = general.ExclusiveCardPool.candidates($proceed()); }");
                    }
                }
            };
        }
    }
}
