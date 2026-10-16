package general;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;

public class PotionDiscardHelper {
    public static boolean discard(AbstractPlayer player, AbstractPotion potion) {
        if (player == null || player != AbstractDungeon.player || potion == null
                || potion instanceof PotionSlot) {
            return false;
        }
        int slot = player.potions.indexOf(potion);
        if (slot < 0) {
            return false;
        }
        // Match PotionPopUp's discard feedback, without drinking or requiring canDiscard().
        CardCrawlGame.sound.play("POTION_DROP_2");
        AbstractDungeon.topPanel.destroyPotion(slot);
        return !player.potions.contains(potion);
    }
}
