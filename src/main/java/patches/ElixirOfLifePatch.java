package patches;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import javassist.CtBehavior;
import potions.ElixirOfLife;

public class ElixirOfLifePatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class PreventDeathPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<Void> insert(AbstractPlayer __instance) {
            if (__instance.currentHealth < 1 && ElixirOfLife.triggerIfPresent()) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasPotion");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "applyEndOfTurnTriggers")
    public static class EndOfPlayerTurnGoldLossPatch {
        @SpirePostfixPatch
        public static void postfix(AbstractCreature __instance) {
            if (__instance instanceof AbstractPlayer) {
                ElixirOfLife.triggerEndOfPlayerTurnGoldLoss();
            }
        }
    }
}
