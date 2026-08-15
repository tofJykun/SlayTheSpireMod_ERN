package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;

import java.util.UUID;

public class IncreaseCostAction extends AbstractGameAction {
    private final UUID cardUuid;
    private final int increaseAmount;

    public IncreaseCostAction(UUID cardUuid, int increaseAmount) {
        this.cardUuid = cardUuid;
        this.increaseAmount = increaseAmount;
    }

    @Override
    public void update() {
        for (AbstractCard card : GetAllInBattleInstances.get(this.cardUuid)) {
            card.modifyCostForCombat(this.increaseAmount);
        }
        this.isDone = true;
    }
}
