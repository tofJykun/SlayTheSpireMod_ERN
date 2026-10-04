package general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.OperationeSolis;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public final class PotionTaskHistory {
    public static final int DAMAGE = 1;
    public static final int ENERGY = 2;
    public static final int DRAW = 4;
    public static final int BLOCK = 8;
    private static final Set<AbstractGameAction> potionActions = Collections.newSetFromMap(new IdentityHashMap<>());
    private static final ArrayDeque<AbstractGameAction> running = new ArrayDeque<>();
    private static int effects;

    private PotionTaskHistory() {}

    public static void reset() {
        potionActions.clear();
        running.clear();
        effects = 0;
    }

    public static void mark(AbstractGameAction action) {
        if (action != null) potionActions.add(action);
    }

    public static void inherit(AbstractGameAction parent, AbstractGameAction child) {
        // Follow only actions queued by the potion action itself, not relic/power/card callbacks.
        if (potionActions.contains(parent)) mark(child);
    }

    public static void begin(AbstractGameAction action) {
        running.push(action);
    }

    public static void end() {
        AbstractGameAction action = running.pop();
        if (action.isDone) potionActions.remove(action);
    }

    public static void record(int effect, int actualAmount) {
        if (actualAmount <= 0 || running.isEmpty() || !potionActions.contains(running.peek())) return;
        effects |= effect;
        if (AbstractDungeon.player != null) {
            AbstractPower quest = AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
            if (quest instanceof OperationeSolis) ((OperationeSolis)quest).checkPotionEffects();
        }
    }

    public static boolean isComplete() {
        return effects == (DAMAGE | ENERGY | DRAW | BLOCK);
    }
}
