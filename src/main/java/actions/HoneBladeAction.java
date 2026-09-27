package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class HoneBladeAction extends AbstractGameAction {
    private final AbstractPlayer player;

    public HoneBladeAction(AbstractPlayer player, int amount) {
        this.player = player;
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.isDone) {
            return;
        }
        this.isDone = true;
        CardGroup candidates = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        // Mirror Violence's candidate insertion and shuffle without moving the real cards.
        for (AbstractCard card : this.player.drawPile.group) {
            if (card.canUpgrade()) {
                candidates.addToRandomSpot(card);
            }
        }
        for (int i = 0; i < this.amount && !candidates.isEmpty(); i++) {
            candidates.shuffle();
            AbstractCard card = candidates.getBottomCard();
            candidates.removeCard(card);
            card.upgrade();
            card.superFlash();
            card.applyPowers();
        }
    }
}
