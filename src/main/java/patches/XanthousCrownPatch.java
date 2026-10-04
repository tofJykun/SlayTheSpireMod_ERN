package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import powers.XanthousCrownPower;
import powers.SilvercatRingPower;

public class XanthousCrownPatch {
    @SpirePatch(clz = AbstractDungeon.class, method = "onModifyPower")
    public static class PowerListChanged {
        @SpirePostfixPatch
        public static void postfix() {
            XanthousCrownPower.checkDexterityChange(AbstractDungeon.player);
            SilvercatRingPower.checkDexterityChange(AbstractDungeon.player);
        }
    }

    @SpirePatches({
            @SpirePatch(clz = DexterityPower.class, method = "stackPower"),
            @SpirePatch(clz = DexterityPower.class, method = "reducePower")
    })
    public static class DexterityChanged {
        @SpirePostfixPatch
        public static void postfix(DexterityPower __instance) {
            if (__instance.owner == AbstractDungeon.player) {
                XanthousCrownPower.checkDexterityChange(__instance.owner);
                SilvercatRingPower.checkDexterityChange(__instance.owner);
            }
        }
    }
}
