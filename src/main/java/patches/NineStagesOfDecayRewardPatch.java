package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import general.CombatState;
import general.ExtraCardRewards;
import general.FragrantBranchOfYoreRewards;

public class NineStagesOfDecayRewardPatch {
    public static void clearRewards(AbstractRoom room, CombatRewardScreen screen) {
        room.rewards.clear();
        screen.rewards.clear();
        screen.hasTakenAll = true;
        ExtraCardRewards.resetCombat();
        FragrantBranchOfYoreRewards.resetCombat();
    }

    @SpirePatch(clz = CombatRewardScreen.class, method = "setupItemReward")
    public static class SkipRewards {
        @SpirePrefixPatch
        public static SpireReturn<Void> prefix(CombatRewardScreen __instance) {
            AbstractRoom room = CombatState.currentRoom();
            if (room != null && room.smoked) {
                clearRewards(room, __instance);
                AbstractDungeon.overlayMenu.proceedButton.show();
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = CombatRewardScreen.class, method = "openCombat", paramtypez = {String.class, boolean.class})
    public static class ClearSmokeRewards {
        @SpirePostfixPatch
        public static void postfix(CombatRewardScreen __instance) {
            AbstractRoom room = CombatState.currentRoom();
            if (room != null && room.smoked) {
                clearRewards(room, __instance);
            }
        }
    }
}
