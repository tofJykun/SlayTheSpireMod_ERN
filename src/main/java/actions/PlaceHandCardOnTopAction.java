package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PlaceHandCardOnTopAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final String prompt;
    private boolean selectionOpened;

    public PlaceHandCardOnTopAction(String prompt) {
        this.player = AbstractDungeon.player;
        this.prompt = prompt;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.player == null || this.player.hand.isEmpty()) {
            this.isDone = true;
            return;
        }

        if (!this.selectionOpened) {
            if (this.player.hand.size() == 1) {
                moveToDrawPile(this.player.hand.getTopCard());
                this.isDone = true;
                return;
            }
            AbstractDungeon.handCardSelectScreen.open(this.prompt, 1, false, false, false, false);
            this.selectionOpened = true;
            tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
            AbstractCard card = AbstractDungeon.handCardSelectScreen.selectedCards.getBottomCard();
            AbstractDungeon.handCardSelectScreen.selectedCards.group.remove(card);
            moveToDrawPile(card);
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            this.isDone = true;
            return;
        }

        tickDuration();
    }

    private void moveToDrawPile(AbstractCard card) {
        if (card == null) {
            return;
        }
        this.player.hand.group.remove(card);
        card.unhover();
        card.stopGlowing();
        this.player.drawPile.addToTop(card);
        this.player.hand.refreshHandLayout();
        this.player.hand.applyPowers();
        this.player.hand.glowCheck();
    }
}
