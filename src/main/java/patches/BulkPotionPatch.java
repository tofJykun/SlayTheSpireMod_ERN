package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
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
}
