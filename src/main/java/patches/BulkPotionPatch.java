package patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.ToyOrnithopter;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import general.BulkPotionQueue;
import potions.ElixirOfLife;

public class BulkPotionPatch {
    @SpirePatch(clz = PotionHelper.class, method = "getPotion")
    public static class GetPotionPatch {
        @SpirePrefixPatch
        public static SpireReturn<AbstractPotion> prefix(String name) {
            if (ElixirOfLife.POTION_ID.equals(name)) {
                return SpireReturn.Return(new ElixirOfLife());
            }
            if (BulkPotionQueue.isBulkPotion(name)) {
                return SpireReturn.Return(BulkPotionQueue.getBulkPotion(name));
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ObtainPotionAction.class, method = "update")
    public static class ObtainThroughSozuPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> prefix(ObtainPotionAction __instance) {
            AbstractPotion potion = ReflectionHacks.getPrivate(__instance, ObtainPotionAction.class, "potion");
            if (potion != null && BulkPotionQueue.isBulkPotion(potion.ID)) {
                if (AbstractDungeon.player != null) {
                    AbstractDungeon.player.obtainPotion(potion);
                }
                __instance.isDone = true;
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ToyOrnithopter.class, method = "onUsePotion")
    public static class SuppressToyOrnithopterPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> prefix() {
            if (AbstractDungeon.topPanel == null || AbstractDungeon.topPanel.potionUi == null) {
                return SpireReturn.Continue();
            }
            AbstractPotion potion = ReflectionHacks.getPrivate(AbstractDungeon.topPanel.potionUi,
                    PotionPopUp.class, "potion");
            if (potion != null && BulkPotionQueue.isBulkPotion(potion.ID)) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
