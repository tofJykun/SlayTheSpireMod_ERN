package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import patches.ScheduledField;

public class PlayScheduledCardAction extends AbstractGameAction {
    private final AbstractCard card;

    public PlayScheduledCardAction(AbstractCard card) {
        this.card = card;
    }

    @Override
    public void update() {
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null || this.card == null || !player.hand.contains(this.card)
                || ScheduledField.getScheduled(this.card) != 0) {
            ScheduledField.setPendingAutoplay(this.card, false);
            this.isDone = true;
            return;
        }

        AbstractMonster target = null;
        if (this.card.target == AbstractCard.CardTarget.ENEMY
                || this.card.target == AbstractCard.CardTarget.SELF_AND_ENEMY) {
            target = AbstractDungeon.getRandomMonster();
            if (target == null) {
                ScheduledField.setPendingAutoplay(this.card, false);
                this.isDone = true;
                return;
            }
        }

        this.card.freeToPlayOnce = true;
        this.card.isInAutoplay = true;
        if (!this.card.canUse(player, target)) {
            this.card.freeToPlayOnce = false;
            this.card.isInAutoplay = false;
            ScheduledField.setPendingAutoplay(this.card, false);
            this.isDone = true;
            return;
        }

        this.card.applyPowers();
        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(this.card, target,
                EnergyPanel.getCurrentEnergy(), true, true), true);
        this.isDone = true;
    }
}
