package patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import powers.AbstractSummonPower;
import summons.SummonAnimationManager;

public class SummonRenderPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "renderPlayerImage",
            paramtypez = { SpriteBatch.class })
    public static class RenderSummonBehindPlayer {
        public static void Prefix(AbstractPlayer __instance, SpriteBatch sb) {
            renderSummon(__instance, sb);
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "loseBlock",
            paramtypez = { int.class, boolean.class })
    public static class RemoveSummonWhenBlockGone {
        public static void Postfix(AbstractCreature __instance, int amount, boolean noAnimation) {
            AbstractSummonPower.removeIfBlockGone(__instance);
        }
    }

    private static void renderSummon(AbstractPlayer player, SpriteBatch sb) {
        AbstractSummonPower summon = AbstractSummonPower.getActiveSummon(player);
        if (summon == null) {
            return;
        }
        SummonAnimationManager.render(player, summon.getSummonKey(), sb);
    }
}
