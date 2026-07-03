package patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.Watcher;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import powers.AbstractSummonPower;

import java.util.HashMap;
import java.util.Map;

public class SummonRenderPatch {
    private static final float SUMMON_ALPHA = 0.5F;
    private static final Map<String, Texture> TEXTURES = new HashMap<>();

    @SpirePatch(clz = AbstractPlayer.class, method = "renderPlayerImage",
            paramtypez = { SpriteBatch.class })
    public static class RenderSummonBehindPlayer {
        public static void Prefix(AbstractPlayer __instance, SpriteBatch sb) {
            renderSummon(__instance, sb);
        }
    }

    @SpirePatch(clz = Watcher.class, method = "renderPlayerImage",
            paramtypez = { SpriteBatch.class })
    public static class RenderSummonBehindWatcher {
        public static void Prefix(Watcher __instance, SpriteBatch sb) {
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

    private static Texture getTexture(String path) {
        Texture texture = TEXTURES.get(path);
        if (texture == null) {
            texture = ImageMaster.loadImage(path);
            TEXTURES.put(path, texture);
        }
        return texture;
    }

    private static void renderSummon(AbstractPlayer player, SpriteBatch sb) {
        AbstractSummonPower summon = AbstractSummonPower.getActiveSummon(player);
        if (summon == null) {
            return;
        }
        Texture texture = getTexture(summon.getSummonImagePath());
        if (texture == null) {
            return;
        }

        Color oldColor = sb.getColor().cpy();
        sb.setColor(1.0F, 1.0F, 1.0F, SUMMON_ALPHA);
        sb.draw(texture,
                player.drawX - texture.getWidth() * Settings.scale / 2.0F + player.animX,
                player.drawY + player.animY,
                texture.getWidth() * Settings.scale,
                texture.getHeight() * Settings.scale,
                0,
                0,
                texture.getWidth(),
                texture.getHeight(),
                player.flipHorizontal,
                player.flipVertical);
        sb.setColor(oldColor);
    }
}
