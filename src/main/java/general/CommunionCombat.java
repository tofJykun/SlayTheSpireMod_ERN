package general;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import powers.CommunionPower;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.WeakHashMap;

/** Carries one attack's damage through deferred actions without replaying its other effects. */
public final class CommunionCombat {
    private static final class Scope {
        final AbstractCard card;
        final boolean randomDamage;
        Scope(AbstractCard card) { this(card, true); }
        Scope(AbstractCard card, boolean randomDamage) { this.card = card; this.randomDamage = randomDamage; }
    }

    private static final Scope NONE = new Scope(null);
    private static final ArrayDeque<Scope> scopes = new ArrayDeque<>();
    private static final Map<AbstractGameAction, Scope> actions = new WeakHashMap<>();
    private static final Map<DamageInfo, Map<AbstractMonster, Integer>> hits = new WeakHashMap<>();
    private static int suspended;

    private CommunionCombat() { }

    public static void reset() {
        scopes.clear();
        actions.clear();
        hits.clear();
        suspended = 0;
    }

    private static boolean enabled() {
        return AbstractDungeon.player != null && CombatState.isInCombat()
                && AbstractDungeon.player.hasPower(CommunionPower.POWER_ID);
    }

    private static AbstractCard current() {
        return suspended == 0 && !scopes.isEmpty() && enabled() ? scopes.peek().card : null;
    }

    public static void beginCard(AbstractCard card) {
        scopes.push(card != null && card.type == AbstractCard.CardType.ATTACK
                && enabled() ? new Scope(card) : NONE);
    }

    public static void endCard() { scopes.pop(); }

    public static void queued(AbstractGameAction action) {
        if (action != null) actions.put(action, current() == null ? NONE : scopes.peek());
    }

    public static void beginAction(AbstractGameAction action) {
        Scope parent = actions.getOrDefault(action, NONE);
        scopes.push(action instanceof DamageAllEnemiesAction || action instanceof ApplyPowerAction
                || action instanceof UseCardAction ? NONE : new Scope(parent.card,
                action.actionType != AbstractGameAction.ActionType.DEBUFF
                        && action.actionType != AbstractGameAction.ActionType.CARD_MANIPULATION));
    }

    public static void endAction(AbstractGameAction action) {
        scopes.pop();
        if (action.isDone) actions.remove(action);
    }

    public static boolean randomAttack() {
        return current() != null && scopes.peek().randomDamage;
    }

    public static void areaDamage() {
        if (!scopes.isEmpty()) {
            scopes.pop();
            scopes.push(NONE);
        }
    }

    public static AbstractMonster firstTarget(MonsterGroup group, AbstractMonster exception) {
        for (AbstractMonster monster : group.monsters) {
            if (monster != exception && living(monster)) return monster;
        }
        return null;
    }

    private static boolean living(AbstractMonster monster) {
        return monster != null && !monster.isDeadOrEscaped() && !monster.halfDead
                && !monster.isDying && monster.currentHealth > 0;
    }

    private static boolean areaCard(AbstractCard card) {
        return ReflectionHacks.<Boolean>getPrivate(card, AbstractCard.class, "isMultiDamage");
    }

    public static void created(DamageInfo info) {
        AbstractCard card = current();
        if (card == null || areaCard(card) || info.owner != AbstractDungeon.player
                || info.type != DamageInfo.DamageType.NORMAL || AbstractDungeon.getMonsters() == null) return;
        // A copy prevents target-dependent calculations from changing the real card's display/state.
        suspended++;
        try {
            Map<AbstractMonster, Integer> damage = new IdentityHashMap<>();
            AbstractCard copy = card.makeStatEquivalentCopy();
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (!living(monster)) continue;
                copy.calculateCardDamage(monster);
                damage.put(monster, copy.damage);
            }
            hits.put(info, damage);
        } finally {
            suspended--;
        }
    }

    /** Custom damage actions may supply their own target-specific calculation. */
    public static void setDamage(DamageInfo info, java.util.function.ToIntFunction<AbstractMonster> calculator) {
        if (!hits.containsKey(info)) return;
        Map<AbstractMonster, Integer> damage = new IdentityHashMap<>();
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (living(monster)) damage.put(monster, calculator.applyAsInt(monster));
        }
        hits.put(info, damage);
    }

    public static void enterDamage() { suspended++; }

    public static void exitDamage(AbstractMonster target, DamageInfo info) {
        suspended--;
        broadcast(target, info);
    }

    public static void broadcast(AbstractMonster target, DamageInfo info) {
        Map<AbstractMonster, Integer> damage = hits.get(info);
        if (suspended != 0 || damage == null || !enabled() || info.owner != AbstractDungeon.player
                || info.type != DamageInfo.DamageType.NORMAL) return;
        suspended++;
        try {
            // The original hit has already resolved once, including all other mods' damage patches.
            for (AbstractMonster monster : new ArrayList<>(AbstractDungeon.getMonsters().monsters)) {
                if (monster == target || !living(monster)) continue;
                Integer amount = damage.get(monster);
                if (amount == null) continue;
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(monster.hb.cX, monster.hb.cY,
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
                DamageInfo extra = new DamageInfo(info.owner, amount, info.type);
                monster.damage(extra);
            }
        } finally {
            suspended--;
        }
    }

    public static boolean keepDamageAction(AbstractGameAction action) {
        return action instanceof DamageAction && current() != null && !areaCard(current())
                && action.target instanceof AbstractMonster && action.source != null && !action.source.isDying
                && AbstractDungeon.getMonsters() != null
                && firstTarget(AbstractDungeon.getMonsters(), null) != null;
    }
}
