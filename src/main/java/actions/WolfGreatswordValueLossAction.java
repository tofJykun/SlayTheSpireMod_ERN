package actions;

import cards.wylder.WolfGreatsword;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;

import java.util.UUID;

public class WolfGreatswordValueLossAction extends AbstractGameAction {
    private final UUID cardUuid;
    private final int lossAmount;

    public WolfGreatswordValueLossAction(UUID cardUuid, int lossAmount) {
        this.cardUuid = cardUuid;
        this.lossAmount = lossAmount;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        for (AbstractCard card : GetAllInBattleInstances.get(this.cardUuid)) {
            if (card instanceof WolfGreatsword) {
                ((WolfGreatsword)card).increaseValueLoss(this.lossAmount);
            }
        }
        this.isDone = true;
    }
}
