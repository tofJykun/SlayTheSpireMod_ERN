package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CombatState;
import powers.PlatingPower;

@SpirePatch(clz = MonsterGroup.class, method = "applyPreTurnLogic")
public class PlatingEnemyTurnPatch {
    // Run once for the group, before the per-monster start-of-turn power loop.
    @SpirePrefixPatch
    public static void prefix(MonsterGroup __instance) {
        if (AbstractDungeon.player == null || !CombatState.isInCombat()
                || CombatState.currentRoom().skipMonsterTurn) {
            return;
        }
        AbstractPower power = AbstractDungeon.player.getPower(PlatingPower.POWER_ID);
        if (power instanceof PlatingPower) {
            ((PlatingPower)power).atStartOfEnemyTurn();
        }
    }
}
