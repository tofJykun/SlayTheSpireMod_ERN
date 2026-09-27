package actions;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import general.SmithingBody;
import java.util.ArrayDeque;
import java.util.ArrayList;

/** Waits for the container and every temporary play to finish before returning originals. */
public final class PackagingSequencer {
    private static final ArrayDeque<Request> requests = new ArrayDeque<>();

    private PackagingSequencer() {}

    public static void request(ArrayList<AbstractCard> originals) {
        requests.addFirst(new Request(new ArrayList<>(originals)));
    }

    public static void clear() { requests.clear(); }

    public static void resumeIfIdle(GameActionManager manager) {
        if (requests.isEmpty() || manager == null) return;
        if (AbstractDungeon.player == null || AbstractDungeon.currMapNode == null
                || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            clear();
            return;
        }
        if ((manager.currentAction != null && !manager.currentAction.isDone)
                || !manager.actions.isEmpty() || !manager.preTurnActions.isEmpty()
                || !manager.cardQueue.isEmpty()) return;

        Request request = requests.peekFirst();
        if (request.index >= request.originals.size() || manager.turnHasEnded
                || manager.cardsPlayedThisTurn.size() >= 999) {
            requests.removeFirst();
            manager.addToBottom(new TakeoutBoxReturnCardsAction(null, request.originals, false));
            return;
        }
        AbstractCard copy = SmithingBody.copy(request.originals.get(request.index++));
        copy.uuid = java.util.UUID.randomUUID();
        copy = SmithingBody.physical(copy);
        copy.current_x = copy.target_x = AbstractDungeon.player.hb.cX;
        copy.current_y = copy.target_y = AbstractDungeon.player.hb.cY;
        copy.targetAngle = 0.0F;
        copy.lighten(false);
        copy.drawScale = 0.12F;
        copy.targetDrawScale = 0.75F;
        copy.freeToPlayOnce = true;
        copy.purgeOnUse = true;
        copy.applyPowers();
        AbstractDungeon.player.limbo.addToBottom(copy);
        boolean targeted = copy.target == AbstractCard.CardTarget.ENEMY
                || copy.target == AbstractCard.CardTarget.SELF_AND_ENEMY;
        if (targeted) {
            manager.addCardQueueItem(new CardQueueItem(copy, true, EnergyPanel.getCurrentEnergy(), true, true), true);
        } else {
            manager.addCardQueueItem(new CardQueueItem(copy, null, EnergyPanel.getCurrentEnergy(), true, true), true);
        }
    }

    private static final class Request {
        private final ArrayList<AbstractCard> originals;
        private int index;
        private Request(ArrayList<AbstractCard> originals) { this.originals = originals; }
    }
}
