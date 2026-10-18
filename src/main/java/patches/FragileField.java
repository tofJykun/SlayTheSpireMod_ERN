package patches;

import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import general.CombatState;
import modifiers.FragileModifier;

public final class FragileField {
    public static final class State {
        public int base;
        public boolean upgraded;
        public int remaining;
        public AbstractCard master;

        public State(int base) {
            this.base = Math.max(0, base);
            remaining = this.base;
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static final SpireField<State> state = new SpireField<>(() -> null);
    }

    private FragileField() {}

    public static State state(AbstractCard card) { return card == null ? null : Fields.state.get(card); }
    public static boolean isFragile(AbstractCard card) { return state(card) != null; }

    public static void setBase(AbstractCard card, int amount) {
        assign(card, new State(amount));
    }

    public static void upgradeBase(AbstractCard card, int amount) {
        State s = state(card);
        if (s == null) return;
        int next = Math.max(0, amount);
        // Preserve uses already spent, shared forged ownership, and the master-deck link.
        s.remaining = (int)Math.max(0L, Math.min(Integer.MAX_VALUE, (long)s.remaining + next - s.base));
        s.base = next;
        s.upgraded = true;
    }

    private static void assign(AbstractCard card, State value) {
        if (card == null) return;
        Fields.state.set(card, value);
        if (value != null && !CardModifierManager.hasModifier(card, FragileModifier.ID)) {
            CardModifierManager.addModifier(card, new FragileModifier());
        }
        if (card instanceof cards.scholar.PowerOfOriginalSin) card.initializeDescription();
    }

    // Only representation changes (smithing/unforging) share ownership, never generated copies.
    public static void share(AbstractCard destination, AbstractCard source) {
        assign(destination, state(source));
    }

    public static void copy(AbstractCard destination, AbstractCard source) {
        State old = state(source);
        State fresh = old == null ? null : new State(old.base);
        if (fresh != null) {
            fresh.remaining = old.remaining;
            fresh.upgraded = old.upgraded;
        }
        assign(destination, fresh);
    }

    public static boolean usesCombatCount(AbstractCard card) {
        return CombatState.isInCombat() && AbstractDungeon.player != null
                && !AbstractDungeon.player.masterDeck.group.contains(card);
    }

    public static int value(AbstractCard card) {
        State s = state(card);
        return s == null ? 0 : usesCombatCount(card) ? s.remaining : s.base;
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class CopyPatch {
        @SpirePostfixPatch
        public static AbstractCard postfix(AbstractCard __result, AbstractCard __instance) {
            copy(__result, __instance);
            return __result;
        }
    }

    @SpirePatch(clz = MakeTempCardInHandAction.class, method = SpirePatch.CLASS)
    public static class ForgedTransfer {
        public static final SpireField<State> state = new SpireField<>(() -> null);
    }

    public static MakeTempCardInHandAction preserveForgedState(MakeTempCardInHandAction action, AbstractCard source) {
        ForgedTransfer.state.set(action, state(source));
        return action;
    }

    @SpirePatch(clz = MakeTempCardInHandAction.class, method = "makeNewCard")
    public static class ForgedTransferCopy {
        @SpirePostfixPatch
        public static AbstractCard postfix(AbstractCard __result, MakeTempCardInHandAction __instance) {
            State s = ForgedTransfer.state.get(__instance);
            if (s != null) assign(__result, s);
            return __result;
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "initializeDeck", paramtypez = {CardGroup.class})
    public static class StartBattle {
        @SpirePostfixPatch
        public static void postfix(CardGroup __instance, CardGroup masterDeck) {
            for (AbstractCard card : __instance.group) {
                State s = state(card);
                if (s == null) continue;
                State fresh = new State(s.base);
                fresh.upgraded = s.upgraded;
                for (AbstractCard master : masterDeck.group) {
                    if (master.uuid.equals(card.uuid)) {
                        fresh.master = master;
                        break;
                    }
                }
                assign(card, fresh);
                card.initializeDescription();
            }
        }
    }
}
