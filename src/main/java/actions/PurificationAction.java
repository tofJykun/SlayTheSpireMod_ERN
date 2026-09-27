package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import general.PurificationDiscardTracker;

import java.util.ArrayList;
import java.util.UUID;

public class PurificationAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final UUID sourceUuid;

    public PurificationAction(AbstractCard source) {
        this.player = AbstractDungeon.player;
        this.sourceUuid = source == null ? null : source.uuid;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.player == null || this.player.discardPile == null) {
            this.isDone = true;
            return;
        }

        ArrayList<AbstractCard> cardsToReturn = new ArrayList<>();
        for (AbstractCard card : this.player.discardPile.group) {
            if (PurificationDiscardTracker.wasDiscardedThisTurn(card)
                    && (this.sourceUuid == null || !this.sourceUuid.equals(card.uuid))) {
                cardsToReturn.add(card);
            }
        }

        for (AbstractCard card : cardsToReturn) {
            if (this.player.hand.size() >= 10) {
                this.player.createHandIsFullDialog();
                break;
            }
            card.unhover();
            card.unfadeOut();
            card.fadingOut = false;
            this.player.discardPile.removeCard(card);
            this.player.hand.addToHand(card);
        }

        this.player.hand.refreshHandLayout();
        this.player.hand.applyPowers();
        this.player.hand.glowCheck();
        this.isDone = true;
    }
}
