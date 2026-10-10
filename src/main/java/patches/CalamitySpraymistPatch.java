package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PoisonPower;
import general.AnomalyTriggerHelper;

@SpirePatch(clz = PoisonPower.class, method = "atStartOfTurn")
public class CalamitySpraymistPatch {
    @SpirePrefixPatch
    public static SpireReturn<Void> prefix(PoisonPower __instance, AbstractCreature ___source) {
        if (AnomalyTriggerHelper.extraRounds(__instance.owner) <= 0) {
            return SpireReturn.Continue();
        }
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            __instance.flashWithoutSound();
            AnomalyTriggerHelper.queueTrigger(__instance, ___source, 1);
        }
        return SpireReturn.Return(null);
    }
}
