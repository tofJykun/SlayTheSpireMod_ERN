package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.CombatState;

public class ElementCopyAction extends AbstractGameAction {
    private final AbstractCard copy;
    private final AbstractMonster target;

    public ElementCopyAction(AbstractCard copy, AbstractMonster target) {
        this.copy = copy;
        this.target = target;
    }

    @Override
    public void update() {
        if (isDone) return;
        isDone = true;
        if (AbstractDungeon.player == null || !CombatState.isInCombat()
                || CombatState.currentRoom().isBattleOver || AbstractDungeon.player.isDead) return;
        boolean targeted = copy.target == AbstractCard.CardTarget.ENEMY
                || copy.target == AbstractCard.CardTarget.SELF_AND_ENEMY;
        if (targeted && (target == null || target.isDeadOrEscaped() || target.isDying || target.halfDead)) return;
        copy.purgeOnUse = true;
        copy.freeToPlayOnce = true;
        copy.isInAutoplay = true;
        copy.dontTriggerOnUseCard = false;
        AbstractDungeon.player.limbo.addToBottom(copy);
        AbstractDungeon.actionManager.addCardQueueItem(
                new CardQueueItem(copy, target, copy.energyOnUse, true, true), true);
    }
}
