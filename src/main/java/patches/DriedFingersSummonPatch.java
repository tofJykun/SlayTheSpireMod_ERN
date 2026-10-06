package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import powers.AbstractSummonPower;
import relics.DriedFingers;

@SpirePatch(clz = AbstractSummonPower.class, method = "triggerSummonEffect")
public class DriedFingersSummonPatch {
    @SpirePostfixPatch
    public static void postfix(AbstractSummonPower __instance) {
        if (AbstractDungeon.player != null && __instance.owner == AbstractDungeon.player
                && AbstractDungeon.player.hasRelic(DriedFingers.ID)) {
            ((DriedFingers)AbstractDungeon.player.getRelic(DriedFingers.ID)).onSummon();
        }
    }
}
