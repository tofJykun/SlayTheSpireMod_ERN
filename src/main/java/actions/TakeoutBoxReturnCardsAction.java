package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import general.PackagingCard;
import basemod.BaseMod;

import java.util.ArrayList;

public class TakeoutBoxReturnCardsAction extends AbstractGameAction {
    private final PackagingCard box;
    private final ArrayList<AbstractCard> cards;
    private final boolean clearBox;

    public TakeoutBoxReturnCardsAction(PackagingCard box, ArrayList<AbstractCard> cards) {
        this(box, cards, true);
    }

    public TakeoutBoxReturnCardsAction(PackagingCard box, ArrayList<AbstractCard> cards, boolean clearBox) {
        this.box = box;
        this.cards = cards;
        this.clearBox = clearBox;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.isDone) return;
        this.isDone = true;
        AbstractPlayer player = AbstractDungeon.player;
        if (player != null) {
            for (AbstractCard card : this.cards) {
                if (player.hand.contains(card) || player.drawPile.contains(card)
                        || player.discardPile.contains(card) || player.exhaustPile.contains(card)) continue;
                card.unhover();
                card.untip();
                card.stopGlowing();
                card.resetAttributes();
                card.applyPowers();
                if (player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                    player.hand.addToBottom(card);
                } else {
                    player.discardPile.addToTop(card);
                }
            }
            player.hand.refreshHandLayout();
        }
        if (this.clearBox && this.box != null) {
            this.box.clearPackagedCards();
        }
        this.isDone = true;
    }
}
