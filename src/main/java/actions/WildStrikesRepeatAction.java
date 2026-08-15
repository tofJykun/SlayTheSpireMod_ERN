package actions;

import cards.duchess.WildStrikes;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class WildStrikesRepeatAction extends AbstractGameAction {
    private final WildStrikes sourceCard;
    private final AbstractMonster targetMonster;
    private final int remainingPlays;

    public WildStrikesRepeatAction(WildStrikes sourceCard, AbstractMonster targetMonster, int remainingPlays) {
        this.sourceCard = sourceCard;
        this.targetMonster = targetMonster;
        this.remainingPlays = remainingPlays;
    }

    @Override
    public void update() {
        if (this.remainingPlays <= 0 || this.sourceCard == null || this.targetMonster == null
                || this.targetMonster.isDeadOrEscaped() || this.targetMonster.isDying || this.targetMonster.halfDead) {
            this.isDone = true;
            return;
        }

        AbstractCard copy = this.sourceCard.makeSameInstanceOf();
        if (copy instanceof WildStrikes) {
            ((WildStrikes)copy).setRemainingPlays(this.remainingPlays);
        }
        copy.purgeOnUse = true;
        copy.freeToPlayOnce = true;
        copy.current_x = this.sourceCard.current_x;
        copy.current_y = this.sourceCard.current_y;
        copy.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        copy.target_y = Settings.HEIGHT / 2.0F;
        copy.calculateCardDamage(this.targetMonster);

        AbstractDungeon.player.limbo.addToBottom(copy);
        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(copy, this.targetMonster, 0, true, true), true);
        this.isDone = true;
    }
}
