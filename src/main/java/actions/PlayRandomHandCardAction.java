package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class PlayRandomHandCardAction extends AbstractGameAction {
    private boolean choosing = false;

    public PlayRandomHandCardAction() {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.player.hand.isEmpty()) {
                this.isDone = true;
                return;
            }

            if (RandomPlayHelper.shouldChooseRandomPlay(AbstractDungeon.player)) {
                String prompt = RandomPlayHelper.consumeInsightPrompt(AbstractDungeon.player);
                AbstractDungeon.handCardSelectScreen.open(prompt, 1, false, false, false, false);
                this.choosing = true;
                tickDuration();
                return;
            }

            AbstractCard card = AbstractDungeon.player.hand.getRandomCard(AbstractDungeon.cardRandomRng);
            queueCard(card);
            tickDuration();
            return;
        }

        if (this.choosing && !AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
                AbstractCard card = AbstractDungeon.handCardSelectScreen.selectedCards.getBottomCard();
                AbstractDungeon.handCardSelectScreen.selectedCards.group.remove(card);
                queueCard(card);
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.isDone = true;
            return;
        }

        tickDuration();
    }

    private void queueCard(AbstractCard card) {
        if (card == null) {
            return;
        }
        if (AbstractDungeon.player.hand.group.contains(card)) {
            AvelynAction.rememberHandPlay(card);
            AbstractDungeon.player.hand.group.remove(card);
        }
        AbstractDungeon.player.limbo.addToBottom(card);
        RandomPlayHelper.prepareRandomPlayedCard(card);

        boolean randomTarget = card.target == AbstractCard.CardTarget.ENEMY
                || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY;
        if (randomTarget) {
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(card, true,
                    EnergyPanel.getCurrentEnergy(), true, true), true);
        } else {
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(card, null,
                    EnergyPanel.getCurrentEnergy(), true, true), true);
        }
        RandomPlayHelper.notifyRandomCardPlayed();
    }
}
