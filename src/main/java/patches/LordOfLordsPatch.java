package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.LordOfLordsProgress;

public final class LordOfLordsPatch {
    @SpirePatch(clz = AbstractDungeon.class, method = "generateSeeds")
    public static class NewRun {
        @SpirePrefixPatch
        public static void prefix() { LordOfLordsProgress.resetRun(); }
    }

    @SpirePatch(clz = ApplyPowerAction.class, method = "update")
    public static class AppliedAilment {
        @SpireInsertPatch(locator = PlayerDebuffStatsPatch.SuccessLocator.class)
        public static void insert(ApplyPowerAction __instance, AbstractPower ___powerToApply) {
            LordOfLordsProgress.onAilment(__instance.target, ___powerToApply, __instance.amount);
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "die", paramtypez = {boolean.class})
    public static class Death {
        @SpirePostfixPatch
        public static void postfix(AbstractMonster __instance) { LordOfLordsProgress.onDeath(__instance); }
    }
}
