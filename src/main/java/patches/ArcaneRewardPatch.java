package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.rewards.RewardItem;
import powers.ArcanePower;

public class ArcaneRewardPatch {
    @SpirePatch(clz = RewardItem.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {})
    public static class CardRewardConstructorPatch {
        @SpirePostfixPatch
        public static void postfix(RewardItem __instance) {
            if (general.ConciliationRewards.suppressCards(general.CombatState.currentRoom())) return;
            ArcanePower.tryAddRareCard(__instance.cards);
        }
    }
}
