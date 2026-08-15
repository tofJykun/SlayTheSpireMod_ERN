package general;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import powers.HaligtreeBalmPower;

public class PotionEffectHelper {
    private PotionEffectHelper() {
    }

    public static boolean hasSacredBarkEffect() {
        return AbstractDungeon.player != null
                && (AbstractDungeon.player.hasRelic("SacredBark")
                || AbstractDungeon.player.hasPower(HaligtreeBalmPower.POWER_ID));
    }

    public static boolean hasHaligtreeBalmOnlyEffect() {
        return AbstractDungeon.player != null
                && !AbstractDungeon.player.hasRelic("SacredBark")
                && AbstractDungeon.player.hasPower(HaligtreeBalmPower.POWER_ID);
    }

    public static void refreshPlayerPotions() {
        if (AbstractDungeon.player == null || AbstractDungeon.player.potions == null) {
            return;
        }
        for (AbstractPotion potion : AbstractDungeon.player.potions) {
            if (potion != null) {
                potion.initializeData();
            }
        }
    }
}
