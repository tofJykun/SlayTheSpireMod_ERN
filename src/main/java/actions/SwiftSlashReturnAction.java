package actions;

import basemod.BaseMod;
import cards.duchess.SwiftSlash;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import patches.ScheduledField;

public class SwiftSlashReturnAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final AbstractCard card;

    public SwiftSlashReturnAction(AbstractPlayer player, AbstractCard card) {
        this.player = player;
        this.card = card;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        this.isDone = true;
        if (this.player == null || this.card == null || this.card.purgeOnUse
                || this.player.hand.contains(this.card)
                || !this.player.discardPile.contains(this.card)) {
            return;
        }
        if (this.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
            this.player.createHandIsFullDialog();
            return;
        }
        this.card.unfadeOut();
        this.card.fadingOut = false;
        this.player.hand.moveToHand(this.card, this.player.discardPile);
        if (this.card instanceof SwiftSlash) {
            ScheduledField.reset(this.card);
        }
        this.player.hand.refreshHandLayout();
        this.player.hand.applyPowers();
        this.player.hand.glowCheck();
        this.player.onCardDrawOrDiscard();
    }
}
