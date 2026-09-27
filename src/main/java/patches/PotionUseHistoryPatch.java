package patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.FairyPotion;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import general.CombatState;
import general.PotionHistory;
import potions.ElixirOfLife;

public class PotionUseHistoryPatch {
    @SpirePatch(clz = PotionPopUp.class, method = "updateInput")
    public static class PotionInputPatch {
        private static int beforePotionUseCount;
        private static AbstractPotion potion;

        @SpirePrefixPatch
        public static void prefix(PotionPopUp __instance) {
            beforePotionUseCount = potionUseCount();
            potion = ReflectionHacks.getPrivate(__instance, PotionPopUp.class, "potion");
        }

        @SpirePostfixPatch
        public static void postfix(PotionPopUp __instance) {
            recordIfPotionWasUsed(beforePotionUseCount, potion, null);
            potion = null;
        }
    }

    @SpirePatch(clz = PotionPopUp.class, method = "updateTargetMode")
    public static class PotionTargetPatch {
        private static int beforePotionUseCount;
        private static AbstractPotion potion;

        @SpirePrefixPatch
        public static void prefix(PotionPopUp __instance) {
            beforePotionUseCount = potionUseCount();
            potion = ReflectionHacks.getPrivate(__instance, PotionPopUp.class, "potion");
        }

        @SpirePostfixPatch
        public static void postfix(PotionPopUp __instance) {
            AbstractMonster target = ReflectionHacks.getPrivate(__instance, PotionPopUp.class, "hoveredMonster");
            recordIfPotionWasUsed(beforePotionUseCount, potion, target);
            potion = null;
        }
    }

    @SpirePatch(clz = FairyPotion.class, method = "use", paramtypez = { AbstractCreature.class })
    public static class FairyPotionUsePatch {
        @SpirePostfixPatch
        public static void postfix(FairyPotion __instance) {
            recordDirectPotionUse(__instance);
        }
    }

    @SpirePatch(clz = ElixirOfLife.class, method = "use", paramtypez = { AbstractCreature.class })
    public static class ElixirOfLifeUsePatch {
        @SpirePostfixPatch
        public static void postfix(ElixirOfLife __instance) {
            recordDirectPotionUse(__instance);
        }
    }

    private static int potionUseCount() {
        return CardCrawlGame.metricData == null ? 0 : CardCrawlGame.metricData.potions_floor_usage.size();
    }

    private static void recordIfPotionWasUsed(int beforePotionUseCount, AbstractPotion potion, AbstractMonster target) {
        if (potion == null
                || CardCrawlGame.metricData == null
                || CardCrawlGame.metricData.potions_floor_usage.size() <= beforePotionUseCount
                || AbstractDungeon.getCurrRoom() == null
                || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            return;
        }
        PotionHistory.record(potion);
        powers.ScattershotThrowPower.onPotionUsed(potion, target);
    }

    private static void recordDirectPotionUse(AbstractPotion potion) {
        if (CombatState.isInCombat()) {
            PotionHistory.record(potion);
            powers.ScattershotThrowPower.onPotionUsed(potion, null);
        }
    }
}
