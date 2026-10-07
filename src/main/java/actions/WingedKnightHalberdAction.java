package actions;

import cards.guardian.WingedKnightHalberd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;

import java.util.ArrayList;

public class WingedKnightHalberdAction extends AbstractGameAction {
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(WingedKnightHalberd.ID);
    private final AbstractPlayer player;
    private final boolean anyNumber;
    private boolean selectionOpen;

    public WingedKnightHalberdAction(AbstractPlayer player, boolean anyNumber) {
        this.player = player;
        this.anyNumber = anyNumber;
        actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (isDone) return;
        if (player == null) {
            isDone = true;
            return;
        }
        if (!selectionOpen) {
            if (player.drawPile.isEmpty()) {
                isDone = true;
                return;
            }
            if (!anyNumber && player.drawPile.size() == 1) {
                moveToDiscard(player.drawPile.getTopCard());
                isDone = true;
                return;
            }
            CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard card : player.drawPile.group) {
                // Match Seek's display without revealing or changing the actual draw order.
                choices.addToRandomSpot(card);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            if (anyNumber) {
                AbstractDungeon.gridSelectScreen.open(choices, choices.size(), true, STRINGS.EXTENDED_DESCRIPTION[1]);
            } else {
                AbstractDungeon.gridSelectScreen.open(choices, 1, STRINGS.EXTENDED_DESCRIPTION[0],
                        false, false, false, false);
            }
            selectionOpen = true;
            return;
        }
        if (AbstractDungeon.isScreenUp) return;

        // selectedCards preserves click order; each move appends to the discard pile's top.
        ArrayList<AbstractCard> selected = new ArrayList<>(AbstractDungeon.gridSelectScreen.selectedCards);
        AbstractDungeon.gridSelectScreen.selectedCards.clear();
        for (AbstractCard card : selected) {
            moveToDiscard(card);
        }
        isDone = true;
    }

    private void moveToDiscard(AbstractCard card) {
        if (player.drawPile.contains(card)) {
            card.unhover();
            card.untip();
            player.drawPile.moveToDiscardPile(card);
        }
    }
}
