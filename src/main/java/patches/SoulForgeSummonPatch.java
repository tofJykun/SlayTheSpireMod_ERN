package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import powers.AbstractSummonPower;
import relics.SoulForge;

@SpirePatch(clz = AbstractSummonPower.class, method = "triggerSummonEffect")
public class SoulForgeSummonPatch {
    @SpirePostfixPatch
    public static void postfix(AbstractSummonPower __instance) {
        if (AbstractDungeon.player != null && __instance.owner == AbstractDungeon.player
                && AbstractDungeon.player.hasRelic(SoulForge.ID)) {
            ((SoulForge)AbstractDungeon.player.getRelic(SoulForge.ID))
                    .onPostPowerApply(__instance, __instance.owner);
        }
    }
}
