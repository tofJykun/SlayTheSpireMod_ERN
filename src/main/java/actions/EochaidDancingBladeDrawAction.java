package actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EochaidDancingBladeDrawAction extends AbstractGameAction {
    private final int remaining;

    public EochaidDancingBladeDrawAction() {
        this(999);
    }

    private EochaidDancingBladeDrawAction(int remaining) {
        this.remaining = remaining;
        this.actionType = ActionType.DRAW;
    }

    @Override
    public void update() {
        this.isDone = true;
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null || this.remaining <= 0 || player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
            return;
        }
        // The draw callback runs before other queued draw effects can replace drawnCards.
        addToTop(new DrawCardAction(1, new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
                if (DrawCardAction.drawnCards.isEmpty()) {
                    return;
                }
                for (AbstractCard card : DrawCardAction.drawnCards) {
                    if (card.type == AbstractCard.CardType.ATTACK) {
                        return;
                    }
                }
                int next = remaining - DrawCardAction.drawnCards.size();
                if (next > 0 && player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                    addToTop(new EochaidDancingBladeDrawAction(next));
                }
            }
        }));
    }
}
