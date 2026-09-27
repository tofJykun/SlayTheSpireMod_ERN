package general;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import java.util.ArrayList;

/** Per-play state, independent of the live cards later moving or changing state. */
public final class PlayedCardHistory {
    private static final ArrayList<AbstractCard> snapshots = new ArrayList<>();
    private static GameActionManager manager;
    private static int turn = -1;
    private static AbstractCard currentPlay;

    private PlayedCardHistory() {}

    private static void syncTurn() {
        if (manager != AbstractDungeon.actionManager || turn != GameActionManager.turn) {
            clear();
            manager = AbstractDungeon.actionManager;
            turn = GameActionManager.turn;
        }
    }

    public static void record(AbstractCard card) {
        syncTurn();
        if (card == null || card.dontTriggerOnUseCard || manager == null) return;
        currentPlay = card;
        snapshots.add(SmithingBody.copy(card));
    }

    public static ArrayList<AbstractCard> beforeCurrentPlay(AbstractCard card) {
        syncTurn();
        ArrayList<AbstractCard> result = new ArrayList<>();
        int end = snapshots.size() - (currentPlay == SmithingBody.physical(card) ? 1 : 0);
        for (int i = 0; i < end; i++) {
            AbstractCard copy = SmithingBody.copy(snapshots.get(i));
            copy.uuid = java.util.UUID.randomUUID();
            result.add(copy);
        }
        return result;
    }

    public static void clear() {
        snapshots.clear();
        currentPlay = null;
        manager = null;
        turn = -1;
    }
}
