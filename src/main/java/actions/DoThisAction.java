package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class DoThisAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final String prompt;

    public DoThisAction(String prompt) {
        this.player = AbstractDungeon.player;
        this.prompt = prompt;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.player == null || this.player.hand.isEmpty()) {
                this.isDone = true;
                return;
            }

            if (this.player.hand.size() == 1) {
                queueCard(this.player.hand.getTopCard());
                this.isDone = true;
                return;
            }

            AbstractDungeon.handCardSelectScreen.open(this.prompt, 1, false, false, false, false);
            tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
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

        AbstractMonster target = null;
        if (card.target == AbstractCard.CardTarget.ENEMY || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY) {
            target = AbstractDungeon.getRandomMonster();
            if (target == null) {
                returnToHand(card);
                return;
            }
        }

        card.freeToPlayOnce = true;
        if (!card.canUse(this.player, target)) {
            card.freeToPlayOnce = false;
            returnToHand(card);
            return;
        }

        AvelynAction.rememberHandPlay(card);
        if (this.player.hand.group.contains(card)) {
            this.player.hand.group.remove(card);
        }
        this.player.limbo.addToBottom(card);
        card.current_x = card.hb.cX;
        card.current_y = card.hb.cY;
        card.target_x = Settings.WIDTH / 2.0F;
        card.target_y = Settings.HEIGHT / 2.0F;
        card.targetAngle = 0.0F;
        card.lighten(false);
        card.drawScale = 0.12F;
        card.targetDrawScale = 0.75F;
        card.applyPowers();
        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(card, target,
                EnergyPanel.getCurrentEnergy(), true, true), true);
    }

    private void returnToHand(AbstractCard card) {
        if (card != null && this.player != null && !this.player.hand.group.contains(card)) {
            this.player.hand.addToTop(card);
            this.player.hand.refreshHandLayout();
        }
    }
}
