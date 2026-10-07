package patches;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;
import powers.FadeIntoShadowPower;

@SpirePatch(clz = AbstractCreature.class, method = "addBlock", paramtypez = {int.class})
public class FadeIntoShadowPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfTurnRelics")
    public static class ExpireBeforeTurnStart {
        @SpirePrefixPatch
        public static void prefix(AbstractPlayer __instance) {
            AbstractPower power = __instance.getPower(FadeIntoShadowPower.POWER_ID);
            if (power instanceof FadeIntoShadowPower) {
                ((FadeIntoShadowPower)power).expire();
            }
        }
    }

    @SpireInsertPatch(locator = Locator.class, localvars = {"tmp"})
    public static void insert(AbstractCreature __instance, @ByRef float[] tmp) {
        if (!__instance.isPlayer) {
            return;
        }
        AbstractPower power = __instance.getPower(FadeIntoShadowPower.POWER_ID);
        if (power instanceof FadeIntoShadowPower) {
            tmp[0] = ((FadeIntoShadowPower)power).amplifyBlock(tmp[0]);
        }
    }

    public static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior method) throws Exception {
            return LineFinder.findInOrder(method, new Matcher.MethodCallMatcher(MathUtils.class, "floor"));
        }
    }
}
