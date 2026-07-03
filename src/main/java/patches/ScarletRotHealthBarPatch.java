package patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import javassist.CtBehavior;
import powers.BloodburnPower;
import powers.ScarletRotPower;

public class ScarletRotHealthBarPatch {
    private static final float HEALTH_BAR_HEIGHT = 20.0F * Settings.scale;
    private static final float HEALTH_BAR_OFFSET_Y = -28.0F * Settings.scale;
    private static final Color POISON_BAR_COLOR = Color.valueOf("78c13c00");
    private static final Color SCARLET_ROT_BAR_COLOR = new Color(120.0F / 255.0F, 40.0F / 255.0F,
            40.0F / 255.0F, 0.0F);
    private static final Color BLOODBURN_BAR_COLOR = new Color(70.0F / 255.0F, 10.0F / 255.0F,
            60.0F / 255.0F, 0.0F);

    @SpirePatch(clz = AbstractCreature.class, method = "renderHealth")
    public static class RenderStatusPreview {
        @SpireInsertPatch(locator = Locator.class, localvars = { "x", "y" })
        public static void insert(AbstractCreature __instance, SpriteBatch sb, float x, float y) {
            if (hasCustomPreview(__instance)) {
                renderStatusPreviewBars(__instance, sb, x, y);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCreature.class, "renderRedHealthBar");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "renderRedHealthBar",
            paramtypez = { SpriteBatch.class, float.class, float.class })
    public static class ClipRedHealthBarForStatusPreview {
        public static SpireReturn<Void> Prefix(AbstractCreature __instance, SpriteBatch sb, float x, float y) {
            if (!hasCustomPreview(__instance)) {
                return SpireReturn.Continue();
            }

            Color barColor = (Color)ReflectionHacks.getPrivate(__instance, AbstractCreature.class,
                    __instance.currentBlock > 0 ? "blueHbBarColor" : "redHbBarColor");
            sb.setColor(barColor);

            int totalPreviewDamage = getTotalPreviewDamage(__instance);
            if (__instance.currentHealth > totalPreviewDamage) {
                float targetWidth = getTargetHealthBarWidth(__instance);
                float previewWidth = getPreviewWidth(__instance, targetWidth, totalPreviewDamage);
                renderHealthBar(__instance, sb, x, y, targetWidth - previewWidth, true, true);
            }
            return SpireReturn.Return(null);
        }
    }

    private static boolean hasCustomPreview(AbstractCreature creature) {
        return creature.hasPower(ScarletRotPower.POWER_ID) || creature.hasPower(BloodburnPower.POWER_ID);
    }

    private static void renderStatusPreviewBars(AbstractCreature creature, SpriteBatch sb, float x, float y) {
        float targetWidth = getTargetHealthBarWidth(creature);
        float cursor = targetWidth;

        int bloodburnDamage = getBloodburnPreviewDamage(creature);
        cursor = renderPreviewSegment(creature, sb, x, y, cursor, bloodburnDamage, BLOODBURN_BAR_COLOR);

        int scarletRotDamage = Math.max(0, getScarletRotPreviewDamage(creature) - bloodburnDamage);
        cursor = renderPreviewSegment(creature, sb, x, y, cursor, scarletRotDamage, SCARLET_ROT_BAR_COLOR);

        int poisonDamage = getPoisonPreviewDamage(creature);
        renderPreviewSegment(creature, sb, x, y, cursor, poisonDamage, POISON_BAR_COLOR);
    }

    private static float renderPreviewSegment(AbstractCreature creature, SpriteBatch sb, float x, float y,
            float cursor, int damage, Color baseColor) {
        if (damage <= 0 || cursor <= 0.0F) {
            return cursor;
        }

        float targetWidth = getTargetHealthBarWidth(creature);
        float width = Math.min(cursor, getPreviewWidth(creature, targetWidth, damage));
        if (width <= 0.0F) {
            return cursor;
        }

        Color color = baseColor.cpy();
        color.a = creature.hbAlpha;
        sb.setColor(color);
        renderHealthBar(creature, sb, x + cursor - width, y, width, cursor - width <= 0.0F,
                cursor == targetWidth);
        return cursor - width;
    }

    private static int getTotalPreviewDamage(AbstractCreature creature) {
        return getPoisonPreviewDamage(creature)
                + Math.max(0, getScarletRotPreviewDamage(creature) - getBloodburnPreviewDamage(creature))
                + getBloodburnPreviewDamage(creature);
    }

    private static int getPoisonPreviewDamage(AbstractCreature creature) {
        if (!creature.hasPower("Poison")) {
            return 0;
        }
        int amount = creature.getPower("Poison").amount;
        if (amount > 0 && creature.hasPower("Intangible")) {
            amount = 1;
        }
        return Math.max(0, amount);
    }

    private static int getScarletRotPreviewDamage(AbstractCreature creature) {
        if (!creature.hasPower(ScarletRotPower.POWER_ID)) {
            return 0;
        }
        int amount = creature.getPower(ScarletRotPower.POWER_ID).amount;
        if (amount <= 0) {
            return 0;
        }
        int damage = amount * 2 - 1;
        if (damage > 0 && creature.hasPower("Intangible")) {
            damage = Math.min(damage, 2);
        }
        return damage;
    }

    private static int getBloodburnPreviewDamage(AbstractCreature creature) {
        if (!creature.hasPower(BloodburnPower.POWER_ID)) {
            return 0;
        }
        int amount = creature.getPower(BloodburnPower.POWER_ID).amount;
        if (amount > 0 && creature.hasPower("Intangible")) {
            amount = 1;
        }
        return Math.max(0, amount);
    }

    private static float getTargetHealthBarWidth(AbstractCreature creature) {
        return (Float)ReflectionHacks.getPrivate(creature, AbstractCreature.class, "targetHealthBarWidth");
    }

    private static float getPreviewWidth(AbstractCreature creature, float targetWidth, int previewDamage) {
        if (creature.currentHealth <= 0) {
            return 0.0F;
        }
        return Math.min(targetWidth, previewDamage / (float)creature.currentHealth * targetWidth);
    }

    private static void renderHealthBar(AbstractCreature creature, SpriteBatch sb, float x, float y, float width,
            boolean drawLeftCap, boolean drawRightCap) {
        if (width <= 0.0F) {
            return;
        }
        if (drawLeftCap && creature.currentHealth > 0) {
            sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT,
                    HEALTH_BAR_HEIGHT);
        }
        sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, width, HEALTH_BAR_HEIGHT);
        if (drawRightCap) {
            sb.draw(ImageMaster.HEALTH_BAR_R, x + width, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT,
                    HEALTH_BAR_HEIGHT);
        }
    }
}
