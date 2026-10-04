package general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.OperationeSolis;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.WeakHashMap;

public final class SolarAttackHistory {
    private static final ArrayDeque<Boolean> contexts = new ArrayDeque<>();
    private static final Set<AbstractGameAction> actions = Collections.newSetFromMap(new IdentityHashMap<>());
    private static final Set<DamageInfo> hits = Collections.newSetFromMap(new WeakHashMap<>());

    private SolarAttackHistory() {}

    public static void reset() {
        contexts.clear(); actions.clear(); hits.clear(); SolarCardBlockHistory.reset();
    }

    public static void beginCard(AbstractCard card) {
        SolarCardBlockHistory.beginCard(card);
        contexts.push(card != null && card.type == AbstractCard.CardType.ATTACK);
    }

    public static void endCard() { contexts.pop(); SolarCardBlockHistory.endCard(); }

    public static void beginAction(AbstractGameAction action) {
        contexts.push(actions.contains(action)); SolarCardBlockHistory.beginAction(action);
    }

    public static void endAction(AbstractGameAction action) {
        SolarCardBlockHistory.endAction(action);
        contexts.pop();
        if (action.isDone) actions.remove(action);
    }

    private static boolean fromAttack() { return !contexts.isEmpty() && contexts.peek(); }

    public static void queued(AbstractGameAction action) {
        SolarCardBlockHistory.queued(action);
        if (action != null && fromAttack()) actions.add(action);
    }

    public static void created(DamageInfo info) {
        if (fromAttack() && info.owner == AbstractDungeon.player && info.type == DamageInfo.DamageType.NORMAL) {
            hits.add(info);
        }
    }

    public static void record(DamageInfo info, int damageAmount) {
        if (damageAmount < 99 || info == null || !hits.contains(info)
                || info.owner != AbstractDungeon.player || info.type != DamageInfo.DamageType.NORMAL
                || AbstractDungeon.player == null) return;
        AbstractPower quest = AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
        if (quest instanceof OperationeSolis) ((OperationeSolis)quest).onOverkillAttack(damageAmount);
    }
}
