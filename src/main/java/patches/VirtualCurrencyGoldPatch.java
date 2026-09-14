package patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import powers.VirtualCurrencyPower;

public class VirtualCurrencyGoldPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "loseGold", paramtypez = { int.class })
    public static class LoseGoldPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(AbstractPlayer __instance, @ByRef int[] goldAmount) {
            if (__instance == null || goldAmount == null || goldAmount.length == 0 || goldAmount[0] <= 0) {
                return SpireReturn.Continue();
            }

            if (!(__instance.getPower(VirtualCurrencyPower.POWER_ID) instanceof VirtualCurrencyPower)) {
                return SpireReturn.Continue();
            }

            VirtualCurrencyPower power = (VirtualCurrencyPower)__instance.getPower(VirtualCurrencyPower.POWER_ID);
            int covered = power.loseTokens(goldAmount[0]);
            goldAmount[0] -= covered;

            if (goldAmount[0] <= 0) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
