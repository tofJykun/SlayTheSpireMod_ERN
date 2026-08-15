package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import general.PotionEffectHelper;

public class HaligtreeBalmPotionPatch {
    @SpirePatch(clz = AbstractPotion.class, method = "getPotency", paramtypez = {})
    public static class TemporarySacredBarkPotencyPatch {
        @SpirePrefixPatch
        public static SpireReturn<Integer> prefix(AbstractPotion __instance) {
            if (PotionEffectHelper.hasHaligtreeBalmOnlyEffect()) {
                return SpireReturn.Return(__instance.getPotency(AbstractDungeon.ascensionLevel) * 2);
            }
            return SpireReturn.Continue();
        }
    }
}
