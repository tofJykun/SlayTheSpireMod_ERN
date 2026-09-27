package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.PlayerDebuffStats;
import javassist.CtBehavior;
import java.util.Arrays;

@SpirePatch(clz = ApplyPowerAction.class, method = "update")
public class PlayerDebuffStatsPatch {
    @SpireInsertPatch(locator = SuccessLocator.class)
    public static void insert(ApplyPowerAction __instance, AbstractPower ___powerToApply) {
        PlayerDebuffStats.recordApplication(__instance.target, ___powerToApply, __instance.amount);
    }

    public static class SuccessLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior method) throws Exception {
            // Both branches are after Artifact/Ginger/Turnip rejection. Exactly one runs.
            int stack = LineFinder.findInOrder(method,
                    new Matcher.MethodCallMatcher(AbstractPower.class, "stackPower"))[0];
            int initial = LineFinder.findInOrder(method,
                    new Matcher.MethodCallMatcher(AbstractPower.class, "onInitialApplication"))[0];
            int[] lines = {stack, initial};
            Arrays.sort(lines);
            return lines;
        }
    }
}
