package actions;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayDeque;

public final class ClockwiseSequencer {
    private static final int MAX_PLAYS_THIS_TURN = 999;
    private static final ArrayDeque<Request> requests = new ArrayDeque<Request>();

    private ClockwiseSequencer() {
    }

    public static void request(int amount) {
        request(amount, false);
    }

    public static void requestRight(int amount) {
        request(amount, true);
    }

    private static void request(int amount, boolean fromRight) {
        if (amount > 0 && AbstractDungeon.player != null
                && AbstractDungeon.player.hand != null
                && !AbstractDungeon.player.hand.isEmpty()
                && AbstractDungeon.actionManager != null
                && AbstractDungeon.actionManager.cardsPlayedThisTurn.size() < MAX_PLAYS_THIS_TURN) {
            requests.addLast(new Request(amount, fromRight));
        }
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

        if (AbstractDungeon.player.hand.isEmpty()) {
            requests.clear();
            return;
        }

        Request current = requests.peekFirst();
        if (current == null || current.remaining <= 0) {
            requests.pollFirst();
            return;
        }

        int index = current.fromRight ? AbstractDungeon.player.hand.group.size() - 1 : 0;
        queueCard(AbstractDungeon.player.hand.group.get(index));
        current.remaining--;
        if (current.remaining <= 0) {
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
        if (AbstractDungeon.player.hand.group.contains(card)) {
            AvelynAction.rememberHandPlay(card);
            AbstractDungeon.player.hand.group.remove(card);
        }
        AbstractDungeon.player.limbo.addToBottom(card);
        prepareAutoplayCard(card);

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
        card.current_x = card.hb.cX;
        card.current_y = card.hb.cY;
        card.target_x = AbstractDungeon.player.hb.cX;
        card.target_y = AbstractDungeon.player.hb.cY;
        card.targetAngle = 0.0F;
        card.lighten(false);
        card.drawScale = 0.12F;
        card.targetDrawScale = 0.75F;
        card.freeToPlayOnce = true;
        card.applyPowers();
    }

    private static class Request {
        private int remaining;
        private final boolean fromRight;

        private Request(int remaining, boolean fromRight) {
            this.remaining = remaining;
            this.fromRight = fromRight;
        }
    }
}
