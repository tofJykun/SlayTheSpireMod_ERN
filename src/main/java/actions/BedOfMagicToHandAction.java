package actions;

import cards.recluse.BedOfMagic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class BedOfMagicToHandAction extends AbstractGameAction {
    public BedOfMagicToHandAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        ArrayList<CardLocation> cardsToMove = new ArrayList<>();
        collect(cardsToMove, AbstractDungeon.player.drawPile);
        collect(cardsToMove, AbstractDungeon.player.discardPile);
        collect(cardsToMove, AbstractDungeon.player.exhaustPile);

        for (CardLocation location : cardsToMove) {
            if (AbstractDungeon.player.hand.size() >= 10) {
                AbstractDungeon.player.createHandIsFullDialog();
                break;
            }
            location.card.unhover();
            location.card.unfadeOut();
            location.card.fadingOut = false;
            location.source.removeCard(location.card);
            AbstractDungeon.player.hand.addToHand(location.card);
        }

        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.player.hand.applyPowers();
        AbstractDungeon.player.hand.glowCheck();
        this.isDone = true;
    }

    private static void collect(ArrayList<CardLocation> cardsToMove, CardGroup group) {
        for (AbstractCard card : group.group) {
            if (BedOfMagic.ID.equals(general.SmithingBody.behavior(card).cardID)) {
                cardsToMove.add(new CardLocation(card, group));
            }
        }
    }

    private static class CardLocation {
        private final AbstractCard card;
        private final CardGroup source;

        private CardLocation(AbstractCard card, CardGroup source) {
            this.card = card;
            this.source = source;
        }
    }
}
