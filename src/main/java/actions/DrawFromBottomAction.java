package actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DrawFromBottomAction extends AbstractGameAction {
    private final int remaining;
    private boolean clearHistory;

    public DrawFromBottomAction(int amount) {
        this(amount, true);
    }

    private DrawFromBottomAction(int amount, boolean clearHistory) {
        remaining = amount;
        this.clearHistory = clearHistory;
        actionType = ActionType.DRAW;
    }

    @Override
    public void update() {
        if (isDone) return;
        if (clearHistory) {
            DrawCardAction.drawnCards.clear();
            clearHistory = false;
        }
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null || remaining <= 0) {
            isDone = true;
            return;
        }
        if (SoulGroup.isActive()) return;
        isDone = true;
        if (player.hasPower("No Draw") || player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
            addToTop(new DrawCardAction(1, false));
            return;
        }
        if (player.drawPile.isEmpty()) {
            if (!player.discardPile.isEmpty()) {
                addToTop(new DrawFromBottomAction(remaining, false));
                addToTop(new EmptyDeckShuffleAction());
            }
            return;
        }
        addToTop(new BottomCardDrawAction(new DrawFromBottomAction(remaining - 1, false)));
    }

    private static final class BottomCardDrawAction extends DrawCardAction {
        private BottomCardDrawAction(AbstractGameAction followUp) {
            super(1, followUp, false);
        }

        @Override
        public void update() {
            if (isDone) return;
            CardGroup pile = AbstractDungeon.player.drawPile;
            AbstractCard bottom = pile.isEmpty() ? null : pile.group.remove(0);
            if (bottom != null) pile.group.add(bottom);
            try {
                // Use native draw hooks/history/animation without a persistent pile reorder.
                super.update();
            } finally {
                if (bottom != null && pile.group.remove(bottom)) pile.group.add(0, bottom);
            }
        }
    }
}
