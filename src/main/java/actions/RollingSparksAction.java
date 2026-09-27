package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import general.BulkPotionQueue;

import java.util.ArrayList;

public class RollingSparksAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int[] damage;
    private final DamageInfo.DamageType damageType;

    public RollingSparksAction(AbstractPlayer player, int[] damage, DamageInfo.DamageType damageType) {
        this.player = player;
        this.damage = damage.clone();
        this.damageType = damageType;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (this.isDone) {
            return;
        }
        this.isDone = true;
        int removed = 0;
        // Discard all bottles before damage can end combat or mutate the potion bar.
        for (AbstractPotion potion : new ArrayList<>(this.player.potions)) {
            if (potion != null && !(potion instanceof PotionSlot)
                    && BulkPotionQueue.isBulkPotion(potion.ID) && this.player.potions.contains(potion)) {
                this.player.removePotion(potion);
                if (!this.player.potions.contains(potion)) {
                    removed++;
                }
            }
        }
        for (int i = 0; i < removed; i++) {
            addToTop(new DamageAllEnemiesAction(this.player, this.damage.clone(), this.damageType,
                    AttackEffect.FIRE));
        }
    }
}
