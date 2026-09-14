package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import general.RelicRewardHelper;

import java.util.ArrayList;

public class BarterAction extends AbstractGameAction {
    public BarterAction() {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST && AbstractDungeon.player != null) {
            ArrayList<AbstractPotion> potions = getAvailablePotions();
            if (!potions.isEmpty()) {
                AbstractPotion potion = potions.get(AbstractDungeon.cardRandomRng.random(potions.size() - 1));
                AbstractDungeon.player.removePotion(potion);
                AbstractRelic relic = RelicRewardHelper.returnRandomEliteDropRelic();
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, relic);
            }
        }
        this.isDone = true;
    }

    private static ArrayList<AbstractPotion> getAvailablePotions() {
        ArrayList<AbstractPotion> potions = new ArrayList<>();
        for (AbstractPotion potion : AbstractDungeon.player.potions) {
            if (!(potion instanceof PotionSlot)) {
                potions.add(potion);
            }
        }
        return potions;
    }
}
