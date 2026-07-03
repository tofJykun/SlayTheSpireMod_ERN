package actions;

import cards.status.MagicEmber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class FistfulOfAshAction extends AbstractGameAction {
    private final int blockPerEmber;

    public FistfulOfAshAction(AbstractCreature target, AbstractCreature source, int blockPerEmber) {
        this.target = target;
        this.source = source;
        this.blockPerEmber = blockPerEmber;
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
            addToBot(new GainBlockAction(this.target, this.source, embers.size() * this.blockPerEmber));
        }
        this.isDone = true;
    }
}
