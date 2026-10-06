package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import powers.AbstractSummonPower;

import java.util.ArrayList;

public class MischiefAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int limit;
    private boolean selectionOpened;

    public MischiefAction(AbstractPlayer player, int limit) {
        this.player = player;
        this.limit = limit;
        actionType = ActionType.EXHAUST;
    }

    @Override
    public void update() {
        if (isDone) return;
        if (!selectionOpened) {
            if (player == null || player.hand.isEmpty() || limit <= 0) {
                isDone = true;
                return;
            }
            selectionOpened = true;
            AbstractDungeon.handCardSelectScreen.open(ExhaustAction.TEXT[0],
                    Math.min(limit, player.hand.size()), true, true);
            return;
        }
        if (AbstractDungeon.isScreenUp) return;
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            // Count selected originals, even if exhaust hooks return them or create other cards.
            ArrayList<AbstractCard> selected = new ArrayList<>(AbstractDungeon.handCardSelectScreen.selectedCards.group);
            AbstractCard phantom = AbstractSummonPower.makeActiveSummonPhantomCard(player);
            for (AbstractCard card : selected) {
                player.hand.moveToExhaustPile(card);
            }
            CardCrawlGame.dungeon.checkForPactAchievement();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            if (phantom != null && !selected.isEmpty()) {
                addToBot(new MakeTempCardInHandAction(phantom, selected.size()));
            }
        }
        isDone = true;
    }
}
