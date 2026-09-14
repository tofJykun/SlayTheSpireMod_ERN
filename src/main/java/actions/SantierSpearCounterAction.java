package actions;

import cards.duchess.SantierSpear;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;

import java.util.UUID;

public class SantierSpearCounterAction extends AbstractGameAction {
    private final UUID cardUuid;

    public SantierSpearCounterAction(UUID cardUuid) {
        this.cardUuid = cardUuid;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        for (AbstractCard card : GetAllInBattleInstances.get(this.cardUuid)) {
            if (card instanceof SantierSpear) {
                ((SantierSpear)card).incrementPlaysThisCombat();
            }
        }
        this.isDone = true;
    }
}
