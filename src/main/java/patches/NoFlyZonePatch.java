package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import powers.NoFlyZonePower;

@SpirePatch(clz = GameActionManager.class, method = "update")
public class NoFlyZonePatch {
    @SpirePostfixPatch
    public static void postfix() {
        NoFlyZonePower.finishHealthChanges();
    }
}
