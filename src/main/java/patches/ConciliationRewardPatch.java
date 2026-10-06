package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import general.CombatState;
import general.ConciliationRewards;

public class ConciliationRewardPatch {
    public static boolean filter(AbstractRoom room, CombatRewardScreen screen) {
        if (room == null) return false;
        if (room.smoked) {
            boolean changed = !room.rewards.isEmpty() || !screen.rewards.isEmpty();
            NineStagesOfDecayRewardPatch.clearRewards(room, screen);
            return changed;
        }
        if (!ConciliationRewards.suppressCards(room)) return false;
        room.rewards.removeIf(r -> r.type == RewardItem.RewardType.CARD);
        boolean changed = screen.rewards.removeIf(r -> r.type == RewardItem.RewardType.CARD);
        screen.hasTakenAll = screen.rewards.isEmpty();
        return changed;
    }

    @SpirePatch(clz = CombatRewardScreen.class, method = "positionRewards")
    public static class Position {
        @SpirePrefixPatch
        public static void prefix(CombatRewardScreen __instance) {
            filter(CombatState.currentRoom(), __instance);
        }
    }

    @SpirePatch(clz = CombatRewardScreen.class, method = "update")
    public static class BeforeInteraction {
        @SpirePrefixPatch
        public static void prefix(CombatRewardScreen __instance) {
            if (filter(CombatState.currentRoom(), __instance)) __instance.positionRewards();
        }
    }
}
