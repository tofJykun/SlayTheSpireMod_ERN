package actions;

import cards.status.MagicEmber;
import cards.tempcards.FadingPrimalGlintstone;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class BedOfMagicEmberToGlintstoneAction extends AbstractGameAction {
    public BedOfMagicEmberToGlintstoneAction() {
        this.actionType = ActionType.EXHAUST;
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> embers = new ArrayList<>();
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (MagicEmber.ID.equals(card.cardID)) {
                embers.add(card);
            }
        }

        for (AbstractCard ember : embers) {
            AbstractDungeon.player.hand.moveToExhaustPile(ember);
        }

        if (!embers.isEmpty()) {
            addToBot(new MakeTempCardInHandAction(new FadingPrimalGlintstone(), embers.size()));
        }
        this.isDone = true;
    }
}
