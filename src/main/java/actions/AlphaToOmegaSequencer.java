package actions;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import general.PlayedCardHistory;
import general.SmithingBody;

import java.util.ArrayDeque;
import java.util.ArrayList;

public final class AlphaToOmegaSequencer {
    private static final int MAX_PLAYS_THIS_TURN = 999;
    private static final ArrayDeque<Request> requests = new ArrayDeque<Request>();

    private AlphaToOmegaSequencer() {
    }

    public static void request(AbstractCard currentCard) {
        if (AbstractDungeon.actionManager == null
                || AbstractDungeon.actionManager.cardsPlayedThisTurn.size() >= MAX_PLAYS_THIS_TURN) {
            return;
        }

        ArrayList<AbstractCard> snapshot = PlayedCardHistory.beforeCurrentPlay(currentCard);

        if (!snapshot.isEmpty()) {
            requests.addFirst(new Request(snapshot));
        }
    }

    public static void clear() {
        requests.clear();
    }

    public static void resumeIfIdle(GameActionManager manager) {
        if (requests.isEmpty() || manager == null || !isCombatActive()) {
            if (!isCombatActive()) {
                requests.clear();
            }
            return;
        }
        if (manager.turnHasEnded || manager.cardsPlayedThisTurn.size() >= MAX_PLAYS_THIS_TURN) {
            requests.clear();
            return;
        }
        if ((manager.currentAction != null && !manager.currentAction.isDone)
                || !manager.actions.isEmpty()
                || !manager.preTurnActions.isEmpty()
                || !manager.cardQueue.isEmpty()) {
            return;
        }

        Request current = requests.peekFirst();
        if (current == null || current.index >= current.cards.size()) {
            requests.pollFirst();
            return;
        }

        queueCard(current.cards.get(current.index));
        current.index++;
        if (current.index >= current.cards.size()) {
            requests.pollFirst();
        }
    }

    private static boolean isCombatActive() {
        return AbstractDungeon.player != null
                && AbstractDungeon.currMapNode != null
                && AbstractDungeon.getCurrRoom() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
    }

    private static void queueCard(AbstractCard card) {
        if (card == null) {
            return;
        }
        card = SmithingBody.physical(card);
        prepareAutoplayCard(card);
        AbstractDungeon.player.limbo.addToBottom(card);

        boolean randomTarget = card.target == AbstractCard.CardTarget.ENEMY
                || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY;
        if (randomTarget) {
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(card, true,
                    EnergyPanel.getCurrentEnergy(), true, true), true);
        } else {
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(card, null,
                    EnergyPanel.getCurrentEnergy(), true, true), true);
        }
    }

    private static void prepareAutoplayCard(AbstractCard card) {
        card.current_x = AbstractDungeon.player.hb.cX;
        card.current_y = AbstractDungeon.player.hb.cY;
        card.target_x = AbstractDungeon.player.hb.cX;
        card.target_y = AbstractDungeon.player.hb.cY;
        card.targetAngle = 0.0F;
        card.lighten(false);
        card.drawScale = 0.12F;
        card.targetDrawScale = 0.75F;
        card.freeToPlayOnce = true;
        card.purgeOnUse = true;
        card.applyPowers();
    }

    private static class Request {
        private final ArrayList<AbstractCard> cards;
        private int index;

        private Request(ArrayList<AbstractCard> cards) {
            this.cards = cards;
            this.index = 0;
        }
    }
}
