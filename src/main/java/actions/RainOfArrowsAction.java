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

public class RainOfArrowsAction extends AbstractGameAction {
    private final AbstractPlayer player;

    public RainOfArrowsAction(int cardsToPlay) {
        this.player = AbstractDungeon.player;
        this.amount = cardsToPlay;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.player == null || this.player.drawPile.isEmpty() || this.amount <= 0) {
            this.isDone = true;
            return;
        }

        for (int i = 0; i < this.amount; i++) {
            AbstractCard card = getRandomAttackFromDrawPile();
            if (card == null) {
                break;
            }
            queueCard(card);
        }
        this.isDone = true;
    }

    private AbstractCard getRandomAttackFromDrawPile() {
        ArrayList<AbstractCard> attacks = new ArrayList<AbstractCard>();
        for (AbstractCard card : this.player.drawPile.group) {
            if (card != null && card.type == AbstractCard.CardType.ATTACK) {
                attacks.add(card);
            }
        }
        if (attacks.isEmpty()) {
            return null;
        }
        return attacks.get(AbstractDungeon.cardRandomRng.random(attacks.size() - 1));
    }

    private void queueCard(AbstractCard card) {
        if (card == null || !this.player.drawPile.group.contains(card)) {
            return;
        }

        AbstractMonster target = null;
        boolean randomTarget = card.target == AbstractCard.CardTarget.ENEMY
                || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY;
        if (randomTarget) {
            target = getFirstLivingMonster();
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
        RandomPlayHelper.prepareRandomPlayedCard(card);

        if (randomTarget) {
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(card, true,
                    EnergyPanel.getCurrentEnergy(), true, true), true);
        } else {
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(card, target,
                    EnergyPanel.getCurrentEnergy(), true, true), true);
        }
        RandomPlayHelper.notifyRandomCardPlayed();
    }

    private AbstractMonster getFirstLivingMonster() {
        if (AbstractDungeon.getMonsters() == null) {
            return null;
        }
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster != null && !monster.isDeadOrEscaped()) {
                return monster;
            }
        }
        return null;
    }
}
