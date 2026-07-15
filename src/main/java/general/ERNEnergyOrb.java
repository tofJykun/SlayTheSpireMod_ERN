package general;

import basemod.abstracts.CustomEnergyOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;

public class ERNEnergyOrb extends CustomEnergyOrb {
    private static final float ORB_IMG_SCALE = 1.15F * Settings.scale;

    public enum Style {
        RED,
        GREEN,
        BLUE,
        PURPLE
    }

    private final Style style;
    private float angle1;
    private float angle2;
    private float angle3;
    private float angle4;
    private float angle5;

    public ERNEnergyOrb(String uiName, Style style) {
        super(texturePaths(uiName), "img/UI_" + uiName + "/energyVFX.png", null);
        this.style = style;
    }

    public static ERNEnergyOrb red(String uiName) {
        return new ERNEnergyOrb(uiName, Style.RED);
    }

    public static ERNEnergyOrb green(String uiName) {
        return new ERNEnergyOrb(uiName, Style.GREEN);
    }

    public static ERNEnergyOrb blue(String uiName) {
        return new ERNEnergyOrb(uiName, Style.BLUE);
    }

    public static ERNEnergyOrb purple(String uiName) {
        return new ERNEnergyOrb(uiName, Style.PURPLE);
    }

    private static String[] texturePaths(String uiName) {
        String panel = "img/UI_" + uiName + "/EPanel/";
        return new String[] {
                panel + "layer1.png",
                panel + "layer2.png",
                panel + "layer3.png",
                panel + "layer4.png",
                panel + "layer5.png",
                panel + "layer0.png",
                panel + "layer1d.png",
                panel + "layer2d.png",
                panel + "layer3d.png",
                panel + "layer4d.png",
                panel + "layer5d.png"
        };
    }

    @Override
    public void updateOrb(int energyCount) {
        float multiplier = energyCount == 0 ? 0.2F : 1.0F;
        switch (style) {
            case RED:
                angle1 += Gdx.graphics.getDeltaTime() * 360.0F * multiplier;
                angle2 += Gdx.graphics.getDeltaTime() * 40.0F * multiplier;
                angle3 += Gdx.graphics.getDeltaTime() * -40.0F * multiplier;
                angle4 += Gdx.graphics.getDeltaTime() * 20.0F * multiplier;
                angle5 += Gdx.graphics.getDeltaTime() * -20.0F * multiplier;
                break;
            case BLUE:
                angle2 += Gdx.graphics.getDeltaTime() * 40.0F * multiplier;
                angle3 += Gdx.graphics.getDeltaTime() * -40.0F * multiplier;
                angle4 += Gdx.graphics.getDeltaTime() * 20.0F * multiplier;
                angle5 += Gdx.graphics.getDeltaTime() * -20.0F * multiplier;
                break;
            case GREEN:
            case PURPLE:
                angle2 += Gdx.graphics.getDeltaTime() * 40.0F * multiplier;
                angle3 += Gdx.graphics.getDeltaTime() * -40.0F * multiplier;
                angle4 += Gdx.graphics.getDeltaTime() * 20.0F * multiplier;
                break;
        }
    }

    @Override
    public void renderOrb(SpriteBatch sb, boolean enabled, float currentX, float currentY) {
        switch (style) {
            case RED:
                renderRed(sb, enabled, currentX, currentY);
                break;
            case GREEN:
                renderGreen(sb, enabled, currentX, currentY);
                break;
            case BLUE:
                renderBlue(sb, enabled, currentX, currentY);
                break;
            case PURPLE:
                renderPurple(sb, enabled, currentX, currentY);
                break;
        }
    }

    private void renderRed(SpriteBatch sb, boolean enabled, float currentX, float currentY) {
        sb.setColor(Color.WHITE);
        Texture[] layers = enabled ? energyLayers : noEnergyLayers;
        draw(sb, layers[0], currentX, currentY, angle1);
        draw(sb, layers[1], currentX, currentY, angle2);
        draw(sb, layers[2], currentX, currentY, angle3);
        draw(sb, layers[3], currentX, currentY, angle4);
        draw(sb, layers[4], currentX, currentY, angle5);
        draw(sb, baseLayer, currentX, currentY, 0.0F);
    }

    private void renderBlue(SpriteBatch sb, boolean enabled, float currentX, float currentY) {
        sb.setColor(Color.WHITE);
        Texture[] layers = enabled ? energyLayers : noEnergyLayers;
        draw(sb, layers[0], currentX, currentY, 0.0F);
        draw(sb, layers[1], currentX, currentY, angle2);
        draw(sb, layers[2], currentX, currentY, angle3);
        draw(sb, layers[3], currentX, currentY, angle4);
        draw(sb, layers[4], currentX, currentY, angle5);
        draw(sb, baseLayer, currentX, currentY, 0.0F);
    }

    private void renderGreen(SpriteBatch sb, boolean enabled, float currentX, float currentY) {
        sb.setColor(Color.WHITE);
        if (enabled) {
            draw(sb, energyLayers[1], currentX, currentY, 0.0F);
            draw(sb, energyLayers[2], currentX, currentY, 0.0F);
            draw(sb, energyLayers[3], currentX, currentY, angle3);
            draw(sb, energyLayers[4], currentX, currentY, 0.0F);
            sb.setBlendFunction(770, 1);
            sb.setColor(Settings.HALF_TRANSPARENT_WHITE_COLOR);
            draw(sb, energyLayers[0], currentX, currentY, angle4);
            sb.setBlendFunction(770, 771);
            sb.setColor(Color.WHITE);
        } else {
            draw(sb, noEnergyLayers[1], currentX, currentY, angle2);
            draw(sb, noEnergyLayers[2], currentX, currentY, 0.0F);
            draw(sb, noEnergyLayers[3], currentX, currentY, angle3);
            draw(sb, noEnergyLayers[4], currentX, currentY, 0.0F);
            draw(sb, noEnergyLayers[0], currentX, currentY, angle4);
        }
        draw(sb, baseLayer, currentX, currentY, 0.0F);
    }

    private void renderPurple(SpriteBatch sb, boolean enabled, float currentX, float currentY) {
        if (enabled) {
            sb.setColor(Color.WHITE);
            draw(sb, energyLayers[0], currentX, currentY, 0.0F);
            draw(sb, energyLayers[1], currentX, currentY, angle2);
            draw(sb, energyLayers[2], currentX, currentY, angle3);
            draw(sb, energyLayers[3], currentX, currentY, angle4);
        } else {
            sb.setColor(Color.GRAY);
            draw(sb, energyLayers[0], currentX, currentY, 0.0F);
        }
        sb.setColor(Color.WHITE);
        draw(sb, baseLayer, currentX, currentY, 0.0F);
    }

    private void draw(SpriteBatch sb, Texture texture, float currentX, float currentY, float angle) {
        float width = texture.getWidth();
        float height = texture.getHeight();
        float halfWidth = width / 2.0F;
        float halfHeight = height / 2.0F;
        sb.draw(texture,
                currentX - halfWidth, currentY - halfHeight,
                halfWidth, halfHeight,
                width, height,
                ORB_IMG_SCALE, ORB_IMG_SCALE,
                angle,
                0, 0,
                (int) width, (int) height,
                false, false);
    }
}
