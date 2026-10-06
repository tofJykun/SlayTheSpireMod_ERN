package actions;

import cards.tempcards.AbstractPhantomCard;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayDeque;
import java.util.ArrayList;

public final class CoordinatedAttackSequencer {
    private static final ArrayDeque<Request> requests = new ArrayDeque<>();

    private CoordinatedAttackSequencer() {}

    public static void request(AbstractPlayer player, AbstractMonster target) {
        if (player == null) return;
        ArrayList<AbstractCard> cards = new ArrayList<>();
        for (AbstractCard card : player.hand.group) {
            if (card instanceof AbstractPhantomCard) cards.add(card);
        }
        if (!cards.isEmpty()) requests.addFirst(new Request(player, target, cards));
    }

    public static void resumeIfIdle(GameActionManager manager) {
        if (requests.isEmpty() || manager == null) return;
        if (AbstractDungeon.player == null || AbstractDungeon.currMapNode == null
                || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT
                || manager.turnHasEnded) {
            requests.clear();
            return;
        }
        // Card effects and replays must settle before selecting the next original.
        if ((manager.currentAction != null && !manager.currentAction.isDone)
                || !manager.actions.isEmpty() || !manager.preTurnActions.isEmpty()
                || !manager.cardQueue.isEmpty()) return;

        Request request = requests.peekFirst();
        while (request.index < request.cards.size()) {
            AbstractCard card = request.cards.get(request.index++);
            AbstractPlayer player = request.player;
            if (player != AbstractDungeon.player || !player.hand.contains(card)) continue;
            boolean targeted = card.target == AbstractCard.CardTarget.ENEMY
                    || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY;
            AbstractMonster target = targeted ? request.target : null;
            if (targeted && (target == null || target.isDeadOrEscaped() || target.isDying || target.halfDead)) continue;

            boolean free = card.freeToPlayOnce;
            boolean autoplay = card.isInAutoplay;
            boolean playable;
            try {
                card.freeToPlayOnce = true;
                card.isInAutoplay = true;
                playable = card.canUse(player, target);
            } finally {
                card.freeToPlayOnce = free;
                card.isInAutoplay = autoplay;
            }
            if (!playable) continue;

            player.hand.group.remove(card);
            AbstractDungeon.getCurrRoom().souls.remove(card);
            player.limbo.addToBottom(card);
            RandomPlayHelper.prepareRandomPlayedCard(card);
            manager.addCardQueueItem(new CardQueueItem(card, target,
                    EnergyPanel.getCurrentEnergy(), true, true), true);
            player.hand.refreshHandLayout();
            return;
        }
        requests.removeFirst();
    }

    private static final class Request {
        private final AbstractPlayer player;
        private final AbstractMonster target;
        private final ArrayList<AbstractCard> cards;
        private int index;

        private Request(AbstractPlayer player, AbstractMonster target, ArrayList<AbstractCard> cards) {
            this.player = player;
            this.target = target;
            this.cards = cards;
        }
    }
}
