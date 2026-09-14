package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayList;

public class BountyAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int goldLossPerCard;

    public BountyAction(int goldLossPerCard) {
        this.player = AbstractDungeon.player;
        this.goldLossPerCard = goldLossPerCard;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.player == null || this.player.drawPile.isEmpty()) {
            this.isDone = true;
            return;
        }

        ArrayList<AbstractCard> cardsToPlay = new ArrayList<AbstractCard>();
        for (AbstractCard card : this.player.drawPile.group) {
            if (card != null && card.type == AbstractCard.CardType.POWER) {
                cardsToPlay.add(card);
            }
        }

        for (AbstractCard card : cardsToPlay) {
            queueCard(card);
        }
        this.isDone = true;
    }

    private void queueCard(AbstractCard card) {
        if (card == null || !this.player.drawPile.group.contains(card)) {
            return;
        }

        AbstractMonster target = null;
        boolean randomTarget = card.target == AbstractCard.CardTarget.ENEMY
                || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY;
        if (randomTarget) {
            target = AbstractDungeon.getRandomMonster();
            if (target == null) {
                return;
            }
        }

        card.freeToPlayOnce = true;
        if (!card.canUse(this.player, target)) {
            card.freeToPlayOnce = false;
            return;
        }

        this.player.drawPile.group.remove(card);
        this.player.limbo.addToBottom(card);
        prepareAutoplayCard(card);
        this.player.loseGold(this.goldLossPerCard);

        if (randomTarget) {
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(card, true,
                    EnergyPanel.getCurrentEnergy(), true, true));
        } else {
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(card, target,
                    EnergyPanel.getCurrentEnergy(), true, true));
        }
    }

    private void prepareAutoplayCard(AbstractCard card) {
        card.current_x = card.hb.cX;
        card.current_y = card.hb.cY;
        card.target_x = Settings.WIDTH / 2.0F;
        card.target_y = Settings.HEIGHT / 2.0F;
        card.targetAngle = 0.0F;
        card.lighten(false);
        card.drawScale = 0.12F;
        card.targetDrawScale = 0.75F;
        card.applyPowers();
    }
}
