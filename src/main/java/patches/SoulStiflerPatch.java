package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import powers.SoulStiflerPower;

@SpirePatch(clz = AbstractCreature.class, method = "addBlock", paramtypez = {int.class})
public class SoulStiflerPatch {
    @SpirePrefixPatch
    public static SpireReturn<Void> prefix(AbstractCreature __instance, int blockAmount) {
        if (__instance instanceof AbstractMonster && __instance.hasPower(SoulStiflerPower.POWER_ID)) {
            if (blockAmount > 0) {
                __instance.getPower(SoulStiflerPower.POWER_ID).flash();
            }
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}
