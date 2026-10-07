package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.megacrit.cardcrawl.orbs.Frost;
import javassist.CtBehavior;

@SpirePatches({
        @SpirePatch(clz = Frost.class, method = "onEvoke"),
        @SpirePatch(clz = Frost.class, method = "onEndOfTurn")
})
public class FrostBlockSourcePatch {
    @SpireRawPatch
    public static void raw(CtBehavior method) throws Exception {
        // Orb block remains non-card block even when a card evokes it.
        method.insertBefore("{ general.SolarCardBlockHistory.beginCard(null); }");
        method.insertAfter("{ general.SolarCardBlockHistory.endCard(); }", true);
    }
}
