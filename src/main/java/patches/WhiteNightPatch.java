package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import general.WhiteNightProgress;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public final class WhiteNightPatch {
    @SpirePatch(clz = AbstractDungeon.class, method = "generateSeeds")
    public static class NewRun {
        @SpirePrefixPatch
        public static void prefix() { WhiteNightProgress.resetRun(); }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "initializeCardPools")
    public static class Pools {
        @SpirePostfixPatch
        public static void postfix() { WhiteNightProgress.prunePools(); }
    }

    // Unlike Discovery, the native transform/general random APIs do not check HEALING.
    @SpirePatch(clz = AbstractDungeon.class, method = "returnTrulyRandomCardFromAvailable",
            paramtypez = {AbstractCard.class, Random.class})
    @SpirePatch(clz = AbstractDungeon.class, method = "returnTrulyRandomCard")
    public static class RandomCandidates {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess field) throws CannotCompileException {
                    if (field.isReader() && field.getClassName().equals(CardGroup.class.getName())
                            && field.getFieldName().equals("group")) {
                        field.replace("{ $_ = general.WhiteNightProgress.candidates($proceed()); }");
                    }
                }
            };
        }
    }
}
