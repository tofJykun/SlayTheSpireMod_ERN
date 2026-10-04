package general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.WeakHashMap;

/** Carries the producer through deferred actions, independently of whose turn it is. */
public final class StatusGenerationSource {
    private static final ArrayDeque<Boolean> contexts = new ArrayDeque<>();
    private static final Map<AbstractGameAction, Boolean> actions = new WeakHashMap<>();
    private static final Map<AbstractPower, Boolean> powers = new WeakHashMap<>();

    private StatusGenerationSource() {}

    public static void reset() { contexts.clear(); actions.clear(); powers.clear(); }

    public static void powerCreated(AbstractPower power, AbstractCreature source) {
        if (power != null) powers.put(power, source == null ? isPlayerSource() : source == AbstractDungeon.player);
    }

    public static boolean isPlayerSource() { return !contexts.isEmpty() && contexts.peek(); }

    public static void begin(Object source) {
        boolean player = source instanceof AbstractCard || source instanceof AbstractRelic
                || source instanceof AbstractPotion;
        if (source instanceof AbstractPower) {
            AbstractPower power = (AbstractPower)source;
            player = AbstractDungeon.player != null && power.owner == AbstractDungeon.player
                    && !Boolean.FALSE.equals(powers.get(power));
        }
        contexts.push(player);
    }

    public static void end() { contexts.pop(); }

    public static void queued(AbstractGameAction action) {
        if (action != null) actions.put(action, isPlayerSource());
    }

    public static void beginAction(AbstractGameAction action) {
        contexts.push(Boolean.TRUE.equals(actions.get(action)));
    }

    public static void endAction(AbstractGameAction action) {
        end();
        if (action.isDone) actions.remove(action);
    }
}
