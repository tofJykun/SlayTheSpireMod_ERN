package general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.WeakHashMap;

/** Keeps each play's cost and pre-consumption Vigor attached to all its hits. */
public final class FrozenNeedleAttackContext {
    private static final ArrayDeque<Integer> contexts = new ArrayDeque<>();
    private static final Map<AbstractGameAction, Integer> actions = new WeakHashMap<>();
    private static final Map<DamageInfo, Integer> hits = new WeakHashMap<>();

    private FrozenNeedleAttackContext() { }

    public static void reset() { contexts.clear(); actions.clear(); hits.clear(); }

    public static void beginCard(AbstractCard card) {
        int cost = 0;
        AbstractPower vigor = AbstractDungeon.player == null ? null
                : AbstractDungeon.player.getPower(VigorPower.POWER_ID);
        if (card != null && card.type == AbstractCard.CardType.ATTACK && vigor != null && vigor.amount > 0) {
            cost = card.cost == -1 ? (card.energyOnUse < 0 ? EnergyPanel.totalCount : card.energyOnUse)
                    : card.costForTurn;
        }
        contexts.push(Math.max(0, cost));
    }

    public static void endCard() { contexts.pop(); }

    public static void beginAction(AbstractGameAction action) {
        // Power application/use callbacks are not the attack's own damage.
        contexts.push(action instanceof ApplyPowerAction || action instanceof UseCardAction
                ? 0 : actions.getOrDefault(action, 0));
    }

    public static void endAction(AbstractGameAction action) {
        contexts.pop();
        if (action.isDone) actions.remove(action);
    }

    public static void queued(AbstractGameAction action) {
        if (action != null && !contexts.isEmpty()) actions.put(action, contexts.peek());
    }

    public static void created(DamageInfo info) {
        if (!contexts.isEmpty() && contexts.peek() > 0 && info.owner == AbstractDungeon.player
                && info.type == DamageInfo.DamageType.NORMAL) hits.put(info, contexts.peek());
    }

    public static int refund(DamageInfo info) {
        if (info == null || AbstractDungeon.player == null || info.owner != AbstractDungeon.player
                || info.type != DamageInfo.DamageType.NORMAL) return 0;
        return hits.getOrDefault(info, 0);
    }
}
