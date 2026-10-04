package general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.OperationeSolis;
import java.util.ArrayDeque;
import java.util.IdentityHashMap;
import java.util.Map;

/** Tracks actual card-origin block, independently of the quest's current stage. */
public final class SolarCardBlockHistory {
    private static final Play NONE = new Play(null);
    private static final ArrayDeque<Play> contexts = new ArrayDeque<>();
    private static final Map<AbstractGameAction, Play> actions = new IdentityHashMap<>();
    private static boolean turnOpen;
    private static boolean gainedBlock;

    private SolarCardBlockHistory() {}

    private static final class Play {
        final OperationeSolis quest;
        boolean damage;
        boolean block;
        Play(OperationeSolis quest) { this.quest = quest; }
        void check() {
            if (quest != null && damage && block && AbstractDungeon.player != null
                    && AbstractDungeon.player.getPower(OperationeSolis.POWER_ID) == quest) quest.onCreationCard();
        }
    }

    public static void reset() {
        contexts.clear();
        actions.clear();
        turnOpen = false;
        gainedBlock = false;
    }

    public static void beginTurn() { turnOpen = true; gainedBlock = false; }

    public static boolean finishTurn() {
        if (!turnOpen) return false;
        turnOpen = false;
        return !gainedBlock;
    }

    public static void beginCard(AbstractCard card) {
        AbstractPower quest = AbstractDungeon.player == null ? null : AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
        contexts.push(card == null ? NONE : new Play(quest instanceof OperationeSolis
                && ((OperationeSolis)quest).getStage() == 12 ? (OperationeSolis)quest : null));
    }
    public static void endCard() { contexts.pop(); }

    public static void beginAction(AbstractGameAction action) {
        // Applying a power or resolving use callbacks is not the card's direct block effect.
        contexts.push(action instanceof ApplyPowerAction || action instanceof UseCardAction
                ? NONE : actions.getOrDefault(action, NONE));
    }

    public static void endAction(AbstractGameAction action) {
        contexts.pop();
        if (action.isDone) actions.remove(action);
    }

    private static boolean fromCard() { return !contexts.isEmpty() && contexts.peek() != NONE; }

    public static void queued(AbstractGameAction action) {
        if (action != null && fromCard()) actions.put(action, contexts.peek());
    }

    public static void nonCardAction(AbstractGameAction action) { actions.remove(action); }

    public static void blockGained(int actualAmount) {
        if (actualAmount <= 0 || !fromCard()) return;
        if (turnOpen) gainedBlock = true;
        Play play = contexts.peek();
        play.block = true;
        play.check();
    }

    public static void enemyDamaged(DamageInfo info, int hpLost) {
        if (hpLost <= 0 || info == null || info.owner != AbstractDungeon.player
                || info.type == DamageInfo.DamageType.HP_LOSS || !fromCard()) return;
        Play play = contexts.peek();
        play.damage = true;
        play.check();
    }
}
