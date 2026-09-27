package actions;

import cards.tempcards.AbstractPhantomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class SharedOrderAction extends AbstractGameAction {
    private final AbstractPlayer player;

    public SharedOrderAction(AbstractPlayer player) {
        this.player = player;
        actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (isDone) {
            return;
        }
        isDone = true;
        if (player == null) {
            return;
        }
        for (AbstractCard card : player.hand.group) {
            if (card instanceof AbstractPhantomCard && card.canUpgrade()) {
                card.upgrade();
                card.superFlash();
                card.applyPowers();
            }
        }
    }
}
