package general;

import basemod.abstracts.CustomSavable;
import cards.scholar.KingdomLegacy;
import cards.scholar.TreeOfLife;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DrawPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import java.util.ArrayList;
import java.util.Collections;

/** Silent, one-combat carryover; no player power is created by Tree of Life itself. */
public final class TreeOfLifeProgress implements CustomSavable<TreeOfLifeProgress.State> {
    public static final String SAVE_KEY = "ERN:TreeOfLife";
    public static class State {
        public boolean earned;
        public ArrayList<CarriedPowerSnapshot> pending = new ArrayList<>();
        public int destinationFloor = -1;
    }
    private static State state = new State();
    private static boolean armed;
    private static boolean restored;
    private static boolean finished;

    public static void resetRun() { state = new State(); armed = restored = finished = false; }
    public static void arm() { armed = true; }

    public static void onCardPlayed(AbstractCard card) {
        if (state.earned || card == null || card.dontTriggerOnUseCard || !patches.SowingField.isSown(card)
                || !KingdomLegacy.ID.equals(SmithingBody.behavior(card).cardID) || AbstractDungeon.player == null) return;
        state.earned = true;
        AbstractCard reward = new TreeOfLife();
        UnlockTracker.markCardAsSeen(reward.cardID);
        CardHelper.obtain(reward.cardID, reward.rarity, reward.color);
        for (AbstractRelic relic : new ArrayList<>(AbstractDungeon.player.relics)) relic.onObtainCard(reward);
        AbstractDungeon.player.masterDeck.addToTop(reward);
        for (AbstractRelic relic : new ArrayList<>(AbstractDungeon.player.relics)) relic.onMasterDeckChange();
        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(reward.makeStatEquivalentCopy()));
    }

    public static void beginCombat() {
        armed = restored = finished = false;
    }

    public static void restoreAtStart() {
        if (restored || AbstractDungeon.loading_post_combat || AbstractDungeon.player == null) return;
        restored = true;
        if (state.pending.isEmpty()) return;
        if (state.destinationFloor != -1 && state.destinationFloor != AbstractDungeon.floorNum) {
            state.pending.clear();
            state.destinationFloor = -1;
            return;
        }
        state.destinationFloor = AbstractDungeon.floorNum;
        ArrayList<AbstractPower> powers = new ArrayList<>();
        for (CarriedPowerSnapshot snapshot : state.pending) powers.add(snapshot.restore());
        // Install the complete snapshot before callbacks so Artifact cannot erase inherited debuffs.
        // Battle-start relics subsequently apply through their ordinary actions and stacking rules.
        AbstractDungeon.player.powers.addAll(powers);
        Collections.sort(AbstractDungeon.player.powers);
        for (AbstractPower power : powers) {
            // Native DrawPower changes hand size in its constructor, unlike RingOfFavor's callback.
            if (power instanceof DrawPower) AbstractDungeon.player.gameHandSize += power.amount;
            power.onInitialApplication();
        }
        AbstractDungeon.onModifyPower();
    }

    public static void finishCombat(AbstractRoom room) {
        if (finished || AbstractDungeon.loading_post_combat || AbstractDungeon.player == null
                || AbstractDungeon.player.isDead || AbstractDungeon.player.isDying) return;
        finished = true;
        ArrayList<CarriedPowerSnapshot> next = new ArrayList<>();
        if (armed) {
            for (AbstractPower power : new ArrayList<>(AbstractDungeon.player.powers)) {
                next.add(CarriedPowerSnapshot.capture(power));
            }
        }
        state.pending = next;
        state.destinationFloor = -1;
        armed = false;
    }

    @Override public State onSave() { return state; }
    @Override public void onLoad(State saved) {
        state = saved == null ? new State() : saved;
        if (state.pending == null) state.pending = new ArrayList<>();
        armed = restored = finished = false;
    }
}
