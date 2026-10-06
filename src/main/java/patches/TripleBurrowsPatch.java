package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class TripleBurrowsPatch {
    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static final SpireField<Boolean> forcedCopy = new SpireField<>(() -> false);
    }

    public static boolean canUse(AbstractCard card, AbstractPlayer player, AbstractMonster target) {
        // Only these two automatic copies bypass playability; keep normal play callbacks enabled.
        return (Fields.forcedCopy.get(card) && card.purgeOnUse && card.isInAutoplay)
                || card.canUse(player, target);
    }

    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class QueueCheck {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall call) throws CannotCompileException {
                    if (call.getClassName().equals(AbstractCard.class.getName()) && call.getMethodName().equals("canUse")) {
                        call.replace("{ $_ = patches.TripleBurrowsPatch.canUse($0, $1, $2); }");
                    }
                }
            };
        }
    }
}
