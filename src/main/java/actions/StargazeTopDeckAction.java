package actions;

import cards.undertaker.Stargaze;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;

import java.util.ArrayList;

public class StargazeTopDeckAction extends AbstractGameAction {
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(Stargaze.ID);

    private final AbstractPlayer player;
    private final ArrayList<AbstractCard> selectedCards = new ArrayList<>();
    private int cardsToChoose;
    private boolean selectionOpen;

    public StargazeTopDeckAction(int cardsToChoose) {
        this.player = AbstractDungeon.player;
        this.cardsToChoose = cardsToChoose;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.player == null || this.cardsToChoose <= 0 || this.player.drawPile.isEmpty()) {
            placeSelectedCardsOnTop();
            this.isDone = true;
            return;
        }

        if (!this.selectionOpen) {
            if (this.player.drawPile.size() == 1) {
                selectCard(this.player.drawPile.getTopCard());
                return;
            }
            AbstractDungeon.gridSelectScreen.open(this.player.drawPile, 1,
                    CARD_STRINGS.EXTENDED_DESCRIPTION[0], false, false, false, false);
            this.selectionOpen = true;
            tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            selectCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.selectionOpen = false;
            this.duration = Settings.ACTION_DUR_FAST;
        }
    }

    private void selectCard(AbstractCard card) {
        if (card == null) {
            return;
        }
        card.unhover();
        card.stopGlowing();
        this.player.drawPile.removeCard(card);
        this.selectedCards.add(card);
        this.cardsToChoose--;
    }

    private void placeSelectedCardsOnTop() {
        for (int i = this.selectedCards.size() - 1; i >= 0; i--) {
            this.player.drawPile.addToTop(this.selectedCards.get(i));
        }
        this.player.drawPile.refreshHandLayout();
        this.player.hand.applyPowers();
        this.player.hand.glowCheck();
    }
}
