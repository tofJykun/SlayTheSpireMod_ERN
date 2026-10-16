package patches;

import cards.ironeye.ProperDeath.NonlethalDamageInfo;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

@SpirePatch(clz = AbstractMonster.class, method = "damage", paramtypez = {DamageInfo.class})
public class ProperDeathDamagePatch {
    @SpireInsertPatch(locator = BeforeHealthLoss.class, localvars = {"damageAmount"})
    public static void insert(AbstractMonster __instance, DamageInfo info, @ByRef int[] damageAmount) {
        if (info instanceof NonlethalDamageInfo && __instance.currentHealth > 0 && damageAmount[0] > 0) {
            int limit = Math.max(0, __instance.currentHealth - ((NonlethalDamageInfo)info).minimumHealth);
            damageAmount[0] = Math.min(damageAmount[0], limit);
        }
    }

    public static class BeforeHealthLoss extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior method) throws Exception {
            // After all damage modifiers, before recording HP loss or triggering death.
            return LineFinder.findInOrder(method,
                    new Matcher.FieldAccessMatcher(AbstractMonster.class, "lastDamageTaken"));
        }
    }
}
