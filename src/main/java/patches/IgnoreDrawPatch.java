package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(clz = AbstractPlayer.class, method = "draw", paramtypez = {int.class})
public final class IgnoreDrawPatch {
    private IgnoreDrawPatch() {}

    public static void afterDraw(AbstractPlayer player, AbstractCard card) {
        if (!IgnoreField.isIgnored(card) || !player.hand.contains(card)) return;
        // Transfer immediately so ignored cards cannot fill the hand during a batch draw.
        player.hand.moveToDiscardPile(card);
        GameActionManager.incrementDiscard(false);
        card.triggerOnManualDiscard();
        AbstractDungeon.actionManager.addToTop(new DrawCardAction(player, 1));
    }

    @SpireInstrumentPatch
    public static ExprEditor instrument() {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall call) throws CannotCompileException {
                if (AbstractCard.class.getName().equals(call.getClassName())
                        && "triggerWhenDrawn".equals(call.getMethodName())) {
                    call.replace("{ if (!patches.IgnoreField.isIgnored($0)) { $proceed($$); } }");
                } else if (CardGroup.class.getName().equals(call.getClassName())
                        && "removeTopCard".equals(call.getMethodName())) {
                    call.replace("{ com.megacrit.cardcrawl.cards.AbstractCard drawn = $0.getTopCard();"
                            + " $proceed($$); patches.IgnoreDrawPatch.afterDraw(this, drawn); }");
                }
            }
        };
    }
}
