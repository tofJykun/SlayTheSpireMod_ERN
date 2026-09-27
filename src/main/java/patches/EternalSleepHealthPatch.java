package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.EternalSleepPower;

@SpirePatch(clz = AbstractCreature.class, method = "healthBarUpdatedEvent", paramtypez = {})
public class EternalSleepHealthPatch {
    @SpirePostfixPatch
    public static void postfix(AbstractCreature __instance) {
        if (__instance instanceof AbstractMonster) {
            AbstractPower power = __instance.getPower(EternalSleepPower.POWER_ID);
            if (power instanceof EternalSleepPower) {
                ((EternalSleepPower)power).onHealthChanged();
            }
        }
    }
}
