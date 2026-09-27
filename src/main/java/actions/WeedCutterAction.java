package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class WeedCutterAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private boolean selectionOpened;

    public WeedCutterAction(AbstractPlayer player) {
        this.player = player;
        this.actionType = ActionType.EXHAUST;
    }

    @Override
    public void update() {
        if (this.isDone) {
            return;
        }
        if (!this.selectionOpened) {
            if (this.player == null || this.player.hand.isEmpty()) {
                this.isDone = true;
                return;
            }
            this.selectionOpened = true;
            AbstractDungeon.handCardSelectScreen.open(ExhaustAction.TEXT[0], this.player.hand.size(), true, true);
            return;
        }
        if (AbstractDungeon.isScreenUp) {
            return;
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            // Selection removes cards from the hand; count these cards, not the exhaust pile delta.
            ArrayList<AbstractCard> selected = new ArrayList<>(AbstractDungeon.handCardSelectScreen.selectedCards.group);
            for (AbstractCard card : selected) {
                this.player.hand.moveToExhaustPile(card);
            }
            CardCrawlGame.dungeon.checkForPactAchievement();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            if (!selected.isEmpty()) {
                addToBot(new GainEnergyAction(selected.size()));
            }
        }
        this.isDone = true;
    }
}
