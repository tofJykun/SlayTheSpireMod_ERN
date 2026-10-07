package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import powers.PowerOfCompassionPower;

@SpirePatch(clz = GameActionManager.class, method = "update")
public class PowerOfCompassionPatch {
    // Action boundaries avoid modifying lists inside ApplyPowerAction's power iteration.
    @SpirePrefixPatch
    public static void prefix() {
        PowerOfCompassionPower.synchronizeAll();
    }

    @SpirePostfixPatch
    public static void postfix() {
        PowerOfCompassionPower.synchronizeAll();
    }
}
