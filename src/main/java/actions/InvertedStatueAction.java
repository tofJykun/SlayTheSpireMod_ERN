package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import patches.OperationeSolisPatch;

import java.util.ArrayList;

public class InvertedStatueAction extends AbstractGameAction {
    private final AbstractPlayer player;

    public InvertedStatueAction(AbstractPlayer player) {
        this.player = player;
        actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (!isDone && player != null) {
            // Both groups store their top at the end. Preserve order and card identity atomically.
            ArrayList<AbstractCard> draw = new ArrayList<>(player.drawPile.group);
            ArrayList<AbstractCard> discard = new ArrayList<>(player.discardPile.group);
            player.drawPile.group.clear();
            player.discardPile.group.clear();
            player.drawPile.group.addAll(discard);
            player.discardPile.group.addAll(draw);
            for (AbstractCard card : draw) card.unhover();
            for (AbstractCard card : discard) card.unhover();
            OperationeSolisPatch.pilesChanged();
            OperationeSolisPatch.recordTopPlacement(player.drawPile);
            player.onCardDrawOrDiscard();
        }
        isDone = true;
    }
}
