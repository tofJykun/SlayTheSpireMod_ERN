package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.ArrayList;

public class DevourerAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int drawAmount;

    public DevourerAction(AbstractPlayer player, int drawAmount) {
        this.player = player;
        this.drawAmount = drawAmount;
        this.actionType = ActionType.EXHAUST;
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> cardsToExhaust = new ArrayList<>(this.player.hand.group);
        for (AbstractCard card : cardsToExhaust) {
            this.player.hand.moveToExhaustPile(card);
        }
        CardCrawlGame.dungeon.checkForPactAchievement();
        addToBot((AbstractGameAction)new DrawCardAction((AbstractCreature)this.player, this.drawAmount));
        this.isDone = true;
    }
}
