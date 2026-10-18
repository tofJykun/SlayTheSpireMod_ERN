package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import general.DaatProgress;

public final class DaatPatch {
    @SpirePatch(clz = AbstractDungeon.class, method = "generateSeeds")
    public static class NewRun {
        @SpirePrefixPatch
        public static void prefix() { DaatProgress.resetRun(); }
    }
}
