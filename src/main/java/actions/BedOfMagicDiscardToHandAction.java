package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class BedOfMagicDiscardToHandAction extends AbstractGameAction {
    public BedOfMagicDiscardToHandAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> cardsToReturn = new ArrayList<>();
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (card.type == AbstractCard.CardType.POWER || card.type == AbstractCard.CardType.STATUS) {
                cardsToReturn.add(card);
            }
        }

        for (AbstractCard card : cardsToReturn) {
            addToBot(new DiscardToHandAction(card));
        }
        this.isDone = true;
    }
}
