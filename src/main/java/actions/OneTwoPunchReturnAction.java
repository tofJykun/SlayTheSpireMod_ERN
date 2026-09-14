package actions;

import basemod.BaseMod;
import cards.raider.BloodforgedSword;
import cards.tempcards.CraftmanCreation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class OneTwoPunchReturnAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final AbstractCard original;

    public OneTwoPunchReturnAction(AbstractPlayer player, AbstractCard card) {
        this.player = player;
        this.original = card;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        this.isDone = true;
        if (this.player == null || this.original == null || this.original.purgeOnUse
                || this.player.hand.contains(this.original)) {
            return;
        }
        CardGroup[] piles = {this.player.discardPile, this.player.exhaustPile, this.player.drawPile};
        for (CardGroup pile : piles) {
            if (pile.contains(this.original)) {
                returnToHand(pile, this.original);
                return;
            }
        }
        // Bloodforged Sword replaces its instance after use, keeping its UUID on the creation.
        if (BloodforgedSword.ID.equals(this.original.cardID)) {
            for (CardGroup pile : piles) {
                for (AbstractCard card : pile.group) {
                    if (card instanceof CraftmanCreation && card.uuid.equals(this.original.uuid)) {
                        returnToHand(pile, card);
                        return;
                    }
                }
            }
        }
    }

    private void returnToHand(CardGroup pile, AbstractCard card) {
        if (this.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
            this.player.createHandIsFullDialog();
            return;
        }
        card.unfadeOut();
        card.fadingOut = false;
        this.player.hand.moveToHand(card, pile);
        this.player.hand.glowCheck();
        this.player.onCardDrawOrDiscard();
    }
}
