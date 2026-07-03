package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class PlayRandomHandCardAction extends AbstractGameAction {
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

            AbstractCard card = AbstractDungeon.player.hand.getRandomCard(AbstractDungeon.cardRandomRng);
            AbstractDungeon.player.hand.group.remove(card);
            AbstractDungeon.player.limbo.addToBottom(card);
            card.current_x = card.hb.cX;
            card.current_y = card.hb.cY;
            card.target_x = Settings.WIDTH / 2.0F;
            card.target_y = Settings.HEIGHT / 2.0F;
            card.targetAngle = 0.0F;
            card.lighten(false);
            card.drawScale = 0.12F;
            card.targetDrawScale = 0.75F;
            card.freeToPlayOnce = true;
            card.applyPowers();

            boolean randomTarget = card.target == AbstractCard.CardTarget.ENEMY
                    || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY;
            if (randomTarget) {
                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(card, true,
                        EnergyPanel.getCurrentEnergy(), true, true), true);
            } else {
                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(card, null,
                        EnergyPanel.getCurrentEnergy(), true, true), true);
            }
        }
        tickDuration();
    }
}
