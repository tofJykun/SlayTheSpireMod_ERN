package actions;

import cards.tempcards.MottledPot;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import general.CrudeDrug;

import java.util.ArrayList;

public class BrewCrudeDrugAction extends AbstractGameAction {
    private final AbstractPlayer player;

    public BrewCrudeDrugAction(AbstractPlayer player) {
        this.player = player;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        ArrayList<String> drugPowerIds = CrudeDrug.firstThreeCrudeDrugPowerIds(this.player);
        if (drugPowerIds.size() >= 3) {
            addToBot(new MakeTempCardInHandAction(new MottledPot(drugPowerIds), 1));
            CrudeDrug.removeFirstThreeCrudeDrugPowers(this.player);
        }
        this.isDone = true;
    }
}
