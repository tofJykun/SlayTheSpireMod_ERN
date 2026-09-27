package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class FistfulOfAshAction extends AbstractGameAction {
    private final int blockPerStatus;

    public FistfulOfAshAction(AbstractCreature target, AbstractCreature source, int blockPerStatus) {
        this.target = target;
        this.source = source;
        this.blockPerStatus = blockPerStatus;
        this.actionType = ActionType.EXHAUST;
    }

    @Override
    public void update() {
        if (this.isDone) {
            return;
        }
        if (AbstractDungeon.player == null) {
            this.isDone = true;
            return;
        }
        ArrayList<AbstractCard> statuses = new ArrayList<>();
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.type == AbstractCard.CardType.STATUS) {
                statuses.add(card);
            }
        }

        int exhaustedCount = 0;
        for (AbstractCard status : statuses) {
            if (AbstractDungeon.player.hand.contains(status)) {
                AbstractDungeon.player.hand.moveToExhaustPile(status);
                exhaustedCount++;
            }
        }

        if (exhaustedCount > 0) {
            addToBot(new GainBlockAction(this.target, this.source, exhaustedCount * this.blockPerStatus));
        }
        this.isDone = true;
    }
}
