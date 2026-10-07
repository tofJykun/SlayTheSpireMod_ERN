package actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class NoMoneyReturnAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final AbstractCard card;

    public NoMoneyReturnAction(AbstractPlayer player, AbstractCard card) {
        this.player = player;
        this.card = card;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.isDone) {
            return;
        }
        this.isDone = true;
        if (this.player == null || this.card == null || this.card.purgeOnUse
                || this.player.hand.contains(this.card)) {
            return;
        }
        CardGroup source = this.player.drawPile.contains(this.card) ? this.player.drawPile
                : this.player.discardPile.contains(this.card) ? this.player.discardPile : null;
        if (source == null) {
            return;
        }
        if (this.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
            this.player.createHandIsFullDialog();
            return;
        }
        this.card.unfadeOut();
        this.card.fadingOut = false;
        this.player.hand.moveToHand(this.card, source);
        this.player.hand.glowCheck();
        this.player.onCardDrawOrDiscard();
    }
}
