package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import general.TreeOfLifeProgress;

public final class TreeOfLifePatch {
    @SpirePatch(clz = SaveAndContinue.class, method = "deleteSave")
    public static class EndRun {
        @SpirePostfixPatch public static void postfix() { TreeOfLifeProgress.resetRun(); }
    }
    @SpirePatch(clz = AbstractDungeon.class, method = "generateSeeds")
    public static class NewRun {
        @SpirePrefixPatch public static void prefix() { TreeOfLifeProgress.resetRun(); }
    }
    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class Unlock {
        @SpirePrefixPatch public static void prefix(AbstractPlayer __instance, AbstractCard card) {
            TreeOfLifeProgress.onCardPlayed(card);
        }
    }
    @SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep")
    public static class Prepare {
        @SpirePrefixPatch public static void prefix() { TreeOfLifeProgress.beginCombat(); }
    }
    @SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfCombatLogic")
    public static class Restore {
        @SpirePrefixPatch public static void prefix() { TreeOfLifeProgress.restoreAtStart(); }
    }
    @SpirePatch(clz = AbstractRoom.class, method = "endBattle")
    public static class Finish {
        @SpirePrefixPatch public static void prefix(AbstractRoom __instance) {
            TreeOfLifeProgress.finishCombat(__instance);
        }
    }
}
