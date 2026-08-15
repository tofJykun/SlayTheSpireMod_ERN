package actions;

import cards.undertaker.Denial;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;

import java.util.ArrayList;

public class DenialAction extends AbstractGameAction {
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(Denial.ID);

    private final AbstractPlayer player;
    private final int cardsToChoose;

    public DenialAction(int cardsToChoose) {
        this.player = AbstractDungeon.player;
        this.cardsToChoose = cardsToChoose;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.player == null || this.player.discardPile.isEmpty()) {
                this.isDone = true;
                return;
            }

            int amount = Math.min(this.cardsToChoose, this.player.discardPile.size());
            if (this.player.discardPile.size() <= this.cardsToChoose) {
                ArrayList<AbstractCard> cardsToMove = new ArrayList<>(this.player.discardPile.group);
                for (AbstractCard card : cardsToMove) {
                    moveToHand(card);
                }
                finishMovingCards();
                this.isDone = true;
                return;
            }

            AbstractDungeon.gridSelectScreen.open(this.player.discardPile, amount,
                    CARD_STRINGS.EXTENDED_DESCRIPTION[0], false, false, false, false);
            tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                moveToHand(card);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            finishMovingCards();
        }
        this.isDone = true;
    }

    private void moveToHand(AbstractCard card) {
        if (this.player.hand.size() >= 10) {
            this.player.createHandIsFullDialog();
            return;
        }
        card.unhover();
        card.unfadeOut();
        card.fadingOut = false;
        this.player.discardPile.removeCard(card);
        this.player.hand.addToHand(card);
    }

    private void finishMovingCards() {
        this.player.hand.refreshHandLayout();
        this.player.hand.applyPowers();
        this.player.hand.glowCheck();
    }
}
