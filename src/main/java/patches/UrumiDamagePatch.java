package patches;

import actions.UrumiAction.UrumiDamageInfo;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

@SpirePatch(clz = AbstractMonster.class, method = "damage", paramtypez = {DamageInfo.class})
public class UrumiDamagePatch {
    @SpireInsertPatch(locator = Locator.class, localvars = {"damageAmount"})
    public static void insert(AbstractMonster __instance, DamageInfo info, int damageAmount) {
        if (info instanceof UrumiDamageInfo) {
            ((UrumiDamageInfo) info).onAttack(damageAmount, __instance);
        }
    }

    public static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior method) throws Exception {
            int line = LineFinder.findInOrder(method,
                    new Matcher.MethodCallMatcher(AbstractPower.class, "onAttack"))[0];
            // After the source-power loop (also reached when the source has no powers).
            return new int[] {line + 2};
        }
    }
}
