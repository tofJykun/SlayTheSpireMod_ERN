package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

/** Plays one attack selected from the hand using the discovery RNG queue. */
public class PlayRandomAttackHandCardAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private boolean selecting;

    public PlayRandomAttackHandCardAction() {
        this.player = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.player == null) {
            this.isDone = true;
            return;
        }
        if (!this.selecting && this.duration == Settings.ACTION_DUR_FAST) {
            CardGroup attacks = attackCardsInHand();
            if (attacks.isEmpty()) {
                this.isDone = true;
                return;
            }
            if (RandomPlayHelper.shouldChooseRandomPlay(this.player) && attacks.size() > 1) {
                this.selecting = true;
                AbstractDungeon.gridSelectScreen.open(attacks, 1,
                        RandomPlayHelper.consumeInsightPrompt(this.player), false, false, false, false);
                tickDuration();
                return;
            }
            AbstractCard card = attacks.group.get(AbstractDungeon.cardRandomRng.random(attacks.size() - 1));
            queueCard(card);
            this.isDone = true;
            return;
        }

        if (this.selecting && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            queueCard(card);
            this.isDone = true;
            return;
        }
        tickDuration();
    }

    private CardGroup attackCardsInHand() {
        CardGroup attacks = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : this.player.hand.group) {
            if (card != null && card.type == AbstractCard.CardType.ATTACK) {
                attacks.addToBottom(card);
            }
        }
        return attacks;
    }

    private void queueCard(AbstractCard card) {
        if (card == null || !this.player.hand.group.contains(card)) {
            return;
        }
        AvelynAction.rememberHandPlay(card);
        this.player.hand.group.remove(card);
        this.player.limbo.addToBottom(card);
        RandomPlayHelper.prepareRandomPlayedCard(card);
        boolean randomTarget = card.target == AbstractCard.CardTarget.ENEMY
                || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY;
        this.player.hand.refreshHandLayout();
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
