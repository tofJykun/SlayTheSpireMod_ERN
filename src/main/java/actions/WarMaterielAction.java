package actions;

import cards.raider.WarMateriel;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;

import java.util.ArrayList;

public class WarMaterielAction extends AbstractGameAction {
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(WarMateriel.ID);

    private final AbstractPlayer player;
    private final int cardsToChoose;
    private boolean selectionOpen;

    public WarMaterielAction(AbstractPlayer player, int cardsToChoose) {
        this.player = player;
        this.cardsToChoose = cardsToChoose;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.player == null || this.cardsToChoose <= 0) {
            this.isDone = true;
            return;
        }

        CardGroup attackCards = buildAttackCards();
        if (attackCards.isEmpty()) {
            this.isDone = true;
            return;
        }

        if (!this.selectionOpen) {
            if (attackCards.size() <= this.cardsToChoose) {
                ArrayList<AbstractCard> cardsToMove = new ArrayList<>(attackCards.group);
                for (AbstractCard card : cardsToMove) {
                    moveToHand(card);
                }
                finishMovingCards();
                this.isDone = true;
                return;
            }

            AbstractDungeon.gridSelectScreen.open(attackCards, this.cardsToChoose,
                    CARD_STRINGS.EXTENDED_DESCRIPTION[0], false, false, false, false);
            this.selectionOpen = true;
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            ArrayList<AbstractCard> selectedCards = new ArrayList<>(AbstractDungeon.gridSelectScreen.selectedCards);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            for (AbstractCard card : selectedCards) {
                moveToHand(card);
            }
            finishMovingCards();
            this.isDone = true;
        }
    }

    private CardGroup buildAttackCards() {
        CardGroup result = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : this.player.discardPile.group) {
            if (card.type == AbstractCard.CardType.ATTACK) {
                result.addToBottom(card);
            }
        }
        return result;
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
