package general;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

public final class SigilOfBaphometRewards implements CustomSavable<Integer> {
    public static final String SAVE_KEY = "ERN:SigilOfBaphometRewards";
    private static int floor = -1;
    private static AbstractRoom victoryRoom;
    private static boolean converted;

    public static void reset() { floor = -1; victoryRoom = null; converted = false; }

    public static void mark(AbstractRoom room) {
        if (room == null || ConciliationRewards.suppressCards(room)) return;
        if (victoryRoom == room && floor == AbstractDungeon.floorNum) return;
        floor = AbstractDungeon.floorNum;
        victoryRoom = room;
        converted = false;
    }

    public static boolean active(AbstractRoom room) {
        return room != null && floor == AbstractDungeon.floorNum
                && (victoryRoom == null || victoryRoom == room) && !ConciliationRewards.suppressCards(room);
    }

    public static void finishSetup(CombatRewardScreen screen) {
        AbstractRoom room = CombatState.currentRoom();
        if (!active(room)) return;
        if (converted) {
            // Reload/rebuilding generates native card rewards again; saved relics already replace them.
            screen.rewards.removeIf(r -> r.type == RewardItem.RewardType.CARD);
            room.rewards.removeIf(r -> r.type == RewardItem.RewardType.CARD);
            screen.positionRewards();
        } else {
            convert(screen);
            converted = true;
        }
    }

    public static void convert(CombatRewardScreen screen) {
        AbstractRoom room = CombatState.currentRoom();
        if (!active(room)) return;
        boolean changed = false;
        for (int i = 0; i < screen.rewards.size(); i++) {
            RewardItem old = screen.rewards.get(i);
            if (old.type != RewardItem.RewardType.CARD) continue;
            RewardItem replacement = new RewardItem(RelicRewardHelper.returnRandomEliteDropRelic());
            UnlockTracker.markRelicAsSeen(replacement.relic.relicId);
            screen.rewards.set(i, replacement);
            // Native postcombat saves read the room, not the visible reward list.
            if (room.rewards != screen.rewards) {
                int roomIndex = room.rewards.indexOf(old);
                if (roomIndex >= 0) room.rewards.set(roomIndex, replacement);
                else room.rewards.add(replacement);
            }
            changed = true;
        }
        if (changed) screen.positionRewards();
    }

    @Override
    public Integer onSave() { return converted && floor == AbstractDungeon.floorNum ? floor : -1; }

    @Override
    public void onLoad(Integer saved) {
        floor = saved == null ? -1 : saved;
        victoryRoom = null;
        converted = floor >= 0;
    }
}
