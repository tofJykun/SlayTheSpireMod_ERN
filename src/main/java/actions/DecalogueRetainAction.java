package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public class DecalogueRetainAction extends AbstractGameAction {
    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString("Decalogue");

    private final AbstractPlayer player;
    private boolean opened;

    public DecalogueRetainAction() {
        this.player = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.player == null || this.player.hand.isEmpty()) {
            this.isDone = true;
            return;
        }

        if (!this.opened) {
            AbstractDungeon.handCardSelectScreen.open(UI_STRINGS.TEXT[0], this.player.hand.size(), true, true);
            this.opened = true;
            tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            ArrayList<AbstractCard> selected = new ArrayList<>(
                    AbstractDungeon.handCardSelectScreen.selectedCards.group);
            for (AbstractCard card : selected) {
                if (!card.isEthereal) {
                    card.retain = true;
                }
                this.player.hand.addToTop(card);
            }
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            this.player.hand.refreshHandLayout();
            this.player.hand.applyPowers();
            this.isDone = true;
            return;
        }

        tickDuration();
    }
}
