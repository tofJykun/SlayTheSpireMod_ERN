package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.PotionHistory;

import java.util.ArrayList;

public class PotionAsceticAction extends AbstractGameAction {
    private final ArrayList<String> potionIds;
    private final AbstractMonster target;
    private int index;

    public PotionAsceticAction(AbstractMonster target) {
        this.potionIds = PotionHistory.snapshot();
        this.target = target;
        this.index = 0;
    }

    @Override
    public void update() {
        if (this.index >= this.potionIds.size()) {
            this.isDone = true;
            return;
        }
        PotionHistory.replayPotion(this.potionIds.get(this.index), this.target);
        this.index++;
    }
}
