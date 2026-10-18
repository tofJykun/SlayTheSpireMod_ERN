package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import general.SigilOfBaphometRewards;

public final class SigilOfBaphometPatch {
    @SpirePatch(clz = AbstractMonster.class, method = SpirePatch.CLASS)
    public static class Banished {
        public static SpireField<Boolean> bySigil = new SpireField<>(() -> false);
    }

    @SpirePatch(clz = MonsterGroup.class, method = "haveMonstersEscaped")
    public static class VictoryRewards {
        @SpirePostfixPatch
        public static boolean postfix(boolean __result, MonsterGroup __instance) {
            if (__result) {
                for (AbstractMonster monster : __instance.monsters) {
                    if (Banished.bySigil.get(monster)) return false;
                }
            }
            return __result;
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "generateSeeds")
    public static class NewRun {
        @SpirePrefixPatch
        public static void prefix() { SigilOfBaphometRewards.reset(); }
    }

    // Open returns after setupItemReward and all extra-card reward postfixes have run.
    @SpirePatch(clz = CombatRewardScreen.class, method = "open", paramtypez = {})
    @SpirePatch(clz = CombatRewardScreen.class, method = "open", paramtypez = {String.class})
    @SpirePatch(clz = CombatRewardScreen.class, method = "openCombat", paramtypez = {String.class, boolean.class})
    public static class Open {
        @SpirePostfixPatch
        public static void postfix(CombatRewardScreen __instance) { SigilOfBaphometRewards.finishSetup(__instance); }
    }

    @SpirePatch(clz = CombatRewardScreen.class, method = "update")
    public static class BeforeInteraction {
        @SpirePrefixPatch
        public static void prefix(CombatRewardScreen __instance) { SigilOfBaphometRewards.convert(__instance); }
    }
}
