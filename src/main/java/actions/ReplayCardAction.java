package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.CombatState;
import patches.ReplayField;

public class ReplayCardAction extends AbstractGameAction {
    private final AbstractCard sourceCard;
    private final AbstractMonster targetMonster;
    private final int remainingPlays;

    public ReplayCardAction(AbstractCard sourceCard, AbstractMonster targetMonster, int remainingPlays) {
        this.sourceCard = sourceCard == null ? null : sourceCard.makeStatEquivalentCopy();
        this.targetMonster = targetMonster;
        this.remainingPlays = remainingPlays;
    }

    @Override
    public void update() {
        if (this.remainingPlays <= 0 || !canReplay()) {
            this.isDone = true;
            return;
        }

        AbstractCard copy = this.sourceCard.makeSameInstanceOf();
        ReplayField.setReplay(copy, 0);
        ReplayField.setReplayCopy(copy, true);
        ReplayField.setRemainingReplay(copy, this.remainingPlays - 1);
        copy.purgeOnUse = true;
        copy.freeToPlayOnce = true;
        copy.current_x = this.sourceCard.current_x;
        copy.current_y = this.sourceCard.current_y;
        copy.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        copy.target_y = Settings.HEIGHT / 2.0F;

        AbstractDungeon.player.limbo.addToBottom(copy);
        if (this.targetMonster != null) {
            copy.calculateCardDamage(this.targetMonster);
        } else {
            copy.applyPowers();
        }
        AbstractDungeon.actionManager.addCardQueueItem(
                new CardQueueItem(copy, this.targetMonster, this.sourceCard.energyOnUse, true, true), true);
        this.isDone = true;
    }

    private boolean canReplay() {
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null || this.sourceCard == null || CombatState.currentRoom() == null) {
            return false;
        }
        if (this.sourceCard.target == AbstractCard.CardTarget.ENEMY
                || this.sourceCard.target == AbstractCard.CardTarget.SELF_AND_ENEMY) {
            if (this.targetMonster == null || this.targetMonster.isDeadOrEscaped()
                    || this.targetMonster.isDying || this.targetMonster.halfDead) {
                return false;
            }
        }

        AbstractCard test = this.sourceCard.makeSameInstanceOf();
        ReplayField.setReplay(test, 0);
        ReplayField.setReplayCopy(test, true);
        test.freeToPlayOnce = true;
        test.purgeOnUse = true;
        test.isInAutoplay = true;
        return test.canUse(player, this.targetMonster);
    }
}
