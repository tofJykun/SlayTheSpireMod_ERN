package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import general.BulkPotionQueue;

import java.util.ArrayList;

public class EquivalentExchangeAction extends AbstractGameAction {
    private final AbstractPlayer player;

    public EquivalentExchangeAction(AbstractPlayer player) {
        this.player = player;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (this.isDone) {
            return;
        }
        this.isDone = true;
        int removed = 0;
        for (AbstractPotion potion : new ArrayList<>(this.player.potions)) {
            if (potion != null && !(potion instanceof PotionSlot)
                    && BulkPotionQueue.isBulkPotion(potion.ID) && this.player.potions.contains(potion)) {
                this.player.removePotion(potion);
                if (!this.player.potions.contains(potion)) {
                    removed++;
                }
            }
        }
        // Remove every old bottle first, then obtain replacements in RNG order.
        ArrayList<AbstractPotion> replacements = new ArrayList<>();
        for (int i = 0; i < removed; i++) {
            replacements.add(BulkPotionQueue.getRandomBulkPotion());
        }
        for (int i = replacements.size() - 1; i >= 0; i--) {
            addToTop(new ObtainPotionAction(replacements.get(i)));
        }
    }
}
