package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import powers.GoldPickledFowlFootPower;

public class GoldPickledFowlFootRewardPatch {
    @SpirePatch(clz = RewardItem.class, method = "applyGoldBonus", paramtypez = {boolean.class})
    public static class ApplyGoldBonusPatch {
        @SpirePostfixPatch
        public static void postfix(RewardItem __instance, boolean theft) {
            if (__instance == null || theft || __instance.type != RewardItem.RewardType.GOLD
                    || AbstractDungeon.player == null
                    || !isMonsterRoom()
                    || !AbstractDungeon.player.hasPower(GoldPickledFowlFootPower.POWER_ID)) {
                return;
            }

            int percent = AbstractDungeon.player.getPower(GoldPickledFowlFootPower.POWER_ID).amount;
            if (percent <= 0) {
                return;
            }

            float multiplier = getExistingGoldDropMultiplier() * (1.0F + percent / 100.0F);
            int multipliedTotal = (int)Math.ceil(__instance.goldAmt * multiplier);
            __instance.bonusGold = Math.max(0, multipliedTotal - __instance.goldAmt);
            if (__instance.bonusGold == 0) {
                __instance.text = __instance.goldAmt + RewardItem.TEXT[1];
            } else {
                __instance.text = __instance.goldAmt + RewardItem.TEXT[1] + " (" + __instance.bonusGold + ")";
            }
        }

        private static float getExistingGoldDropMultiplier() {
            float multiplier = 1.0F;
            if (AbstractDungeon.player.hasRelic("Golden Idol")) {
                multiplier *= 1.25F;
            }
            if (ModHelper.isModEnabled("Midas")) {
                multiplier *= 3.0F;
            }
            if (ModHelper.isModEnabled("MonsterHunter")) {
                multiplier *= 2.5F;
            }
            return multiplier;
        }

        private static boolean isMonsterRoom() {
            if (AbstractDungeon.currMapNode == null) {
                return false;
            }
            AbstractRoom room = AbstractDungeon.currMapNode.getRoom();
            return room instanceof MonsterRoom;
        }
    }
}
