package patches;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;
import powers.ParryPower;
import powers.PerfectGuardPower;

public class PerfectGuardPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class BeforeBlockPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = { "damageAmount" })
        public static void insert(AbstractPlayer __instance, DamageInfo info, int damageAmount) {
            ParryPower.triggerIfParry(__instance, info, damageAmount);
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class AfterAttackPatch {
        @SpirePostfixPatch
        public static void postfix(AbstractPlayer __instance, DamageInfo info) {
            if (info != null && info.owner instanceof AbstractMonster && info.type == DamageInfo.DamageType.NORMAL) {
                PerfectGuardPower.triggerIfPerfectGuard(__instance, (AbstractMonster)info.owner);
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            try {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "decrementBlock");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            } catch (Exception ignored) {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCreature.class, "decrementBlock");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }
}
