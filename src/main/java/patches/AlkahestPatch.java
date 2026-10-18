package patches;

import actions.AlkahestAction;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import general.AlkahestProgress;

public final class AlkahestPatch {
    @SpirePatch(clz = AbstractDungeon.class, method = "generateSeeds")
    public static class NewRun {
        @SpirePrefixPatch
        public static void prefix() { AlkahestProgress.resetRun(); }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfTurnPostDrawRelics")
    public static class AfterOpeningDraw {
        @SpirePostfixPatch
        public static void postfix() {
            AbstractDungeon.actionManager.addToBottom(new AlkahestAction());
        }
    }
}
