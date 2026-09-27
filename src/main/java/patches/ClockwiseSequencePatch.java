package patches;

import actions.AlphaToOmegaSequencer;
import actions.ClockwiseSequencer;
import actions.PackagingSequencer;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;

public class ClockwiseSequencePatch {
    @SpirePatch(clz = GameActionManager.class, method = "update")
    public static class UpdatePatch {
        @SpirePostfixPatch
        public static void postfix(GameActionManager __instance) {
            PackagingSequencer.resumeIfIdle(__instance);
            ClockwiseSequencer.resumeIfIdle(__instance);
            AlphaToOmegaSequencer.resumeIfIdle(__instance);
        }
    }
}
