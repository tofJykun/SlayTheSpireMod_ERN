package actions;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import general.CombatState;

import java.util.ArrayDeque;
import java.util.ArrayList;

public final class AvelynSequencer {
    private static final ArrayDeque<Request> requests = new ArrayDeque<>();
    private static boolean resolvingCard;

    private AvelynSequencer() {}

    public static boolean isResolvingCard() { return resolvingCard; }

    public static void request(AbstractPlayer player, ArrayList<AbstractCard> cards, AbstractCard copy) {
        if (!cards.isEmpty() || copy != null) requests.addLast(new Request(player, cards, copy));
    }

    public static void clear() {
        requests.clear();
        resolvingCard = false;
        AvelynAction.clear();
    }

    public static void resumeIfIdle(GameActionManager manager) {
        if (requests.isEmpty() || manager == null) return;
        AbstractPlayer player = AbstractDungeon.player;
        if (!CombatState.isInCombat() || player == null || player.isDying
                || manager.turnHasEnded || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            clear();
            return;
        }
        // Wait for damage, card movement, and any third-party replays to finish.
        if ((manager.currentAction != null && !manager.currentAction.isDone)
                || !manager.actions.isEmpty() || !manager.preTurnActions.isEmpty()
                || !manager.cardQueue.isEmpty()) return;
        resolvingCard = false;
        Request request = requests.peekFirst();
        if (request.player != player) {
            clear();
            return;
        }
        while (!request.cards.isEmpty()) {
            AbstractCard card = request.cards.remove(AbstractDungeon.cardRandomRng.random(request.cards.size() - 1));
            if (!player.drawPile.contains(card) && !player.discardPile.contains(card)) continue;
            AbstractMonster target = firstLivingMonster();
            if (target == null) { clear(); return; }
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

            AvelynAction.forgetHandPlay(card);
            player.drawPile.group.remove(card);
            player.discardPile.group.remove(card);
            AbstractDungeon.getCurrRoom().souls.remove(card);
            card.unhover();
            card.untip();
            card.stopGlowing();
            card.unfadeOut();
            RandomPlayHelper.prepareRandomPlayedCard(card);
            player.limbo.addToBottom(card);
            resolvingCard = true;
            manager.addCardQueueItem(new CardQueueItem(card, true,
                    EnergyPanel.getCurrentEnergy(), true, true), true);
            RandomPlayHelper.notifyRandomCardPlayed();
            return;
        }
        requests.removeFirst();
        if (request.copy != null) {
            manager.addToBottom(new MakeTempCardInDrawPileAction(request.copy, 1, true, true));
        }
    }

    private static AbstractMonster firstLivingMonster() {
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped() && !monster.isDying && !monster.halfDead) return monster;
        }
        return null;
    }

    private static final class Request {
        final AbstractPlayer player;
        final ArrayList<AbstractCard> cards;
        final AbstractCard copy;

        Request(AbstractPlayer player, ArrayList<AbstractCard> cards, AbstractCard copy) {
            this.player = player;
            this.cards = new ArrayList<>(cards);
            this.copy = copy;
        }
    }
}
