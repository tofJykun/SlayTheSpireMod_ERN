package general;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/** Tracks the player's cards that entered the discard pile during the current turn. */
public final class PurificationDiscardTracker {
    private static final Set<UUID> discardedThisTurn = new HashSet<>();
    private static int trackedTurn = Integer.MIN_VALUE;
    private static AbstractRoom trackedRoom;

    private PurificationDiscardTracker() {
    }

    public static void record(AbstractCard card) {
        if (card == null || AbstractDungeon.player == null || !CombatState.isInCombat()
                || AbstractDungeon.player.hand == null) {
            return;
        }
        AbstractRoom currentRoom = CombatState.currentRoom();
        if (currentRoom != trackedRoom || GameActionManager.turn != trackedTurn) {
            discardedThisTurn.clear();
            trackedRoom = currentRoom;
            trackedTurn = GameActionManager.turn;
        }
        discardedThisTurn.add(card.uuid);
    }

    public static boolean wasDiscardedThisTurn(AbstractCard card) {
        synchronizeTurn();
        return card != null && discardedThisTurn.contains(card.uuid);
    }

    private static void synchronizeTurn() {
        AbstractRoom currentRoom = CombatState.currentRoom();
        if (currentRoom != trackedRoom || GameActionManager.turn != trackedTurn) {
            discardedThisTurn.clear();
            trackedRoom = currentRoom;
            trackedTurn = GameActionManager.turn;
        }
    }
}
