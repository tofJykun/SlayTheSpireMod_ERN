package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.ChargedHelper;

public class ChargedCardUsePatch {
    private static final Color BLUE_BORDER_GLOW_COLOR = new Color(0.2F, 0.9F, 1.0F, 0.25F);

    @SpirePatch(clz = AbstractCard.class, method = "canUse", paramtypez = { AbstractPlayer.class, AbstractMonster.class })
    public static class CanUse {
        public static boolean Postfix(boolean __result, AbstractCard __instance, AbstractPlayer p, AbstractMonster m) {
            if (!__result || !ChargedHelper.isChargedCard(__instance)) {
                return __result;
            }
            if (!ChargedHelper.isHandFull(p)) {
                __instance.cantUseMessage = ChargedHelper.cantUseMessage();
                return false;
            }
            return true;
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "triggerOnGlowCheck")
    public static class Glow {
        public static void Postfix(AbstractCard __instance) {
            if (!ChargedHelper.isChargedCard(__instance)) {
                return;
            }
            __instance.glowColor = ChargedHelper.isHandFull(AbstractDungeon.player)
                    ? Color.GOLD.cpy()
                    : BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}
