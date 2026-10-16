package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import general.BulkPotionQueue;
import general.PotionDiscardHelper;

import java.util.ArrayList;

public class CatStateAction extends AbstractGameAction {
    private final AbstractPlayer player;

    public CatStateAction(AbstractPlayer player) {
        this.player = player;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (this.isDone) {
            return;
        }
        this.isDone = true;
        if (this.player == null) {
            return;
        }
        for (AbstractPotion potion : new ArrayList<>(this.player.potions)) {
            PotionDiscardHelper.discard(this.player, potion);
        }
        AbstractPotion chosen = BulkPotionQueue.getRandomBulkPotion();
        // One RNG choice for the entire belt; each slot receives its own potion instance.
        for (AbstractPotion potion : this.player.potions) {
            if (potion instanceof PotionSlot) {
                addToTop(new ObtainPotionAction(chosen.makeCopy()));
            }
        }
    }
}
