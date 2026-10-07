package actions;

import cards.ironeye.Avelyn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import general.SmithingBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class AvelynAction extends AbstractGameAction {
    private static final Set<AbstractCard> handPlays = Collections.newSetFromMap(new WeakHashMap<>());
    private final AbstractPlayer player;
    private final ArrayList<AbstractCard> cards = new ArrayList<>();
    private final AbstractCard copy;
    private final boolean recruitedPlay;

    public AvelynAction(AbstractPlayer player, AbstractCard source) {
        this.player = player;
        AbstractCard physical = SmithingBody.physical(source);
        boolean fromHand = handPlays.remove(physical) || player.hand.contains(physical);
        recruitedPlay = !fromHand && AvelynSequencer.isResolvingCard();
        copy = fromHand && !recruitedPlay ? source.makeStatEquivalentCopy() : null;
        if (!recruitedPlay) {
            collect(player.drawPile.group, physical);
            collect(player.discardPile.group, physical);
        }
    }

    private void collect(ArrayList<AbstractCard> pile, AbstractCard source) {
        for (AbstractCard card : pile) {
            if (card != source && Avelyn.ID.equals(SmithingBody.behavior(card).cardID)
                    && !cards.contains(card)) cards.add(card);
        }
    }

    // Hand autoplay helpers move cards into limbo before AbstractCard.use is called.
    public static void rememberHandPlay(AbstractCard card) {
        if (card != null && Avelyn.ID.equals(SmithingBody.behavior(card).cardID)) {
            handPlays.add(SmithingBody.physical(card));
        }
    }

    public static void clear() { handPlays.clear(); }

    public static void forgetHandPlay(AbstractCard card) { handPlays.remove(SmithingBody.physical(card)); }

    @Override
    public void update() {
        if (isDone) return;
        isDone = true;
        if (!recruitedPlay) AvelynSequencer.request(player, cards, copy);
    }
}
