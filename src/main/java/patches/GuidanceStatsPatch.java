package patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.actions.defect.ShuffleAllAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import general.DeadEnemyStats;
import general.GuidanceStats;

public class GuidanceStatsPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "damage", paramtypez = { DamageInfo.class })
    public static class PlayerDamagePatch {
        @SpirePostfixPatch
        public static void postfix(AbstractPlayer __instance, DamageInfo info) {
            GuidanceStats.recordDamage(__instance, info, __instance.lastDamageTaken);
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage", paramtypez = { DamageInfo.class })
    public static class MonsterDamagePatch {
        @SpirePostfixPatch
        public static void postfix(AbstractMonster __instance, DamageInfo info) {
            GuidanceStats.recordDamage(__instance, info, __instance.lastDamageTaken);
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "die", paramtypez = { boolean.class })
    public static class MonsterDieWithRelicPatch {
        @SpirePostfixPatch
        public static void postfix(AbstractMonster __instance, boolean triggerRelics) {
            GuidanceStats.recordMonsterDeath(__instance);
            DeadEnemyStats.recordMonsterDeath(__instance);
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "addBlock", paramtypez = { int.class })
    public static class BlockPatch {
        private static int beforeBlock;

        @SpirePrefixPatch
        public static void prefix(AbstractCreature __instance, int blockAmount) {
            beforeBlock = __instance.currentBlock;
        }

        @SpirePostfixPatch
        public static void postfix(AbstractCreature __instance, int blockAmount) {
            GuidanceStats.recordBlockGain(__instance, beforeBlock, __instance.currentBlock);
        }
    }

    @SpirePatch(clz = PotionPopUp.class, method = "updateInput")
    public static class PotionInputPatch {
        private static int beforePotionUseCount;

        @SpirePrefixPatch
        public static void prefix(PotionPopUp __instance) {
            beforePotionUseCount = CardCrawlGame.metricData == null
                    ? 0
                    : CardCrawlGame.metricData.potions_floor_usage.size();
        }

        @SpirePostfixPatch
        public static void postfix(PotionPopUp __instance) {
            recordIfPotionWasUsed(beforePotionUseCount);
        }
    }

    @SpirePatch(clz = PotionPopUp.class, method = "updateTargetMode")
    public static class PotionTargetPatch {
        private static int beforePotionUseCount;

        @SpirePrefixPatch
        public static void prefix(PotionPopUp __instance) {
            beforePotionUseCount = CardCrawlGame.metricData == null
                    ? 0
                    : CardCrawlGame.metricData.potions_floor_usage.size();
        }

        @SpirePostfixPatch
        public static void postfix(PotionPopUp __instance) {
            recordIfPotionWasUsed(beforePotionUseCount);
        }
    }

    private static void recordIfPotionWasUsed(int beforePotionUseCount) {
        if (CardCrawlGame.metricData == null
                || CardCrawlGame.metricData.potions_floor_usage.size() <= beforePotionUseCount
                || AbstractDungeon.getCurrRoom() == null
                || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            return;
        }
        GuidanceStats.recordPotionUse();
    }

    @SpirePatch(clz = EmptyDeckShuffleAction.class, method = SpirePatch.CONSTRUCTOR)
    public static class EmptyDeckShufflePatch {
        @SpirePostfixPatch
        public static void postfix(EmptyDeckShuffleAction __instance) {
            recordShuffleIfInCombat();
        }
    }

    @SpirePatch(clz = ShuffleAllAction.class, method = SpirePatch.CONSTRUCTOR)
    public static class ShuffleAllPatch {
        @SpirePostfixPatch
        public static void postfix(ShuffleAllAction __instance) {
            recordShuffleIfInCombat();
        }
    }

    @SpirePatch(clz = ShuffleAction.class, method = "update")
    public static class ShuffleActionPatch {
        @SpirePrefixPatch
        public static void prefix(ShuffleAction __instance) {
            Boolean triggerRelics = ReflectionHacks.getPrivate(__instance, ShuffleAction.class, "triggerRelics");
            if (triggerRelics != null && triggerRelics.booleanValue()) {
                recordShuffleIfInCombat();
            }
        }
    }

    private static void recordShuffleIfInCombat() {
        if (AbstractDungeon.getCurrRoom() == null
                || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            return;
        }
        GuidanceStats.recordShuffle();
    }
}
