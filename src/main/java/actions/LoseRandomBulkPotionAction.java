package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import general.BulkPotionQueue;

import java.util.ArrayList;

public class LoseRandomBulkPotionAction extends AbstractGameAction {
    public LoseRandomBulkPotionAction() {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST && AbstractDungeon.player != null) {
            ArrayList<AbstractPotion> potions = getAvailableBulkPotions();
            if (!potions.isEmpty()) {
                AbstractPotion potion = potions.get(AbstractDungeon.cardRandomRng.random(potions.size() - 1));
                AbstractDungeon.player.removePotion(potion);
            }
        }
        this.isDone = true;
    }

    public static boolean hasBulkPotion() {
        if (AbstractDungeon.player == null) {
            return false;
        }
        return !getAvailableBulkPotions().isEmpty();
    }

    private static ArrayList<AbstractPotion> getAvailableBulkPotions() {
        ArrayList<AbstractPotion> potions = new ArrayList<>();
        if (AbstractDungeon.player == null) {
            return potions;
        }
        for (AbstractPotion potion : AbstractDungeon.player.potions) {
            if (!(potion instanceof PotionSlot) && potion != null && BulkPotionQueue.isBulkPotion(potion.ID)) {
                potions.add(potion);
            }
        }
        return potions;
    }
}
