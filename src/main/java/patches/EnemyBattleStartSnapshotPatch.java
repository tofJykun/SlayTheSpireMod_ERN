package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import general.EnemyBattleStartSnapshot;

public class EnemyBattleStartSnapshotPatch {
    @SpirePatch(clz = MonsterRoom.class, method = "onPlayerEntry")
    public static class MonsterRoomEntryPatch {
        @SpirePrefixPatch
        public static void prefix(MonsterRoom __instance) {
            if (__instance.monsters == null) {
                EnemyBattleStartSnapshot.prepareForRoomGeneration();
            } else {
                EnemyBattleStartSnapshot.reset();
            }
        }
    }

    @SpirePatch(clz = MonsterRoomElite.class, method = "onPlayerEntry")
    public static class MonsterRoomEliteEntryPatch {
        @SpirePrefixPatch
        public static void prefix(MonsterRoomElite __instance) {
            if (__instance.monsters == null) {
                EnemyBattleStartSnapshot.prepareForRoomGeneration();
            } else {
                EnemyBattleStartSnapshot.reset();
            }
        }
    }

    @SpirePatch(clz = MonsterRoomBoss.class, method = "onPlayerEntry")
    public static class MonsterRoomBossEntryPatch {
        @SpirePrefixPatch
        public static void prefix(MonsterRoomBoss __instance) {
            EnemyBattleStartSnapshot.prepareForRoomGeneration();
        }
    }

    @SpirePatch(clz = MonsterGroup.class, method = "showIntent")
    public static class ShowIntentPatch {
        @SpirePostfixPatch
        public static void postfix(MonsterGroup __instance) {
            if (GameActionManager.turn <= 1 && AbstractDungeon.getCurrRoom() != null
                    && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                EnemyBattleStartSnapshot.captureIfNeeded();
            }
        }
    }
}
