package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.CombatState;
import patches.TripleBurrowsPatch;

public class TripleBurrowsAction extends AbstractGameAction {
    private final AbstractCard copy;
    private final AbstractMonster target;

    public TripleBurrowsAction(AbstractCard copy, AbstractMonster target) {
        this.copy = copy;
        this.target = target;
    }

    @Override
    public void update() {
        isDone = true;
        if (AbstractDungeon.player == null || AbstractDungeon.player.isDead || !CombatState.isInCombat()
                || CombatState.currentRoom().isBattleOver) return;
        boolean targeted = copy.target == AbstractCard.CardTarget.ENEMY
                || copy.target == AbstractCard.CardTarget.SELF_AND_ENEMY;
        if (targeted && (target == null || target.isDeadOrEscaped() || target.isDying || target.halfDead)) return;
        copy.purgeOnUse = true;
        copy.freeToPlayOnce = true;
        copy.isInAutoplay = true;
        copy.dontTriggerOnUseCard = false;
        TripleBurrowsPatch.Fields.forcedCopy.set(copy, true);
        copy.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        copy.target_y = Settings.HEIGHT / 2.0F;
        if (target != null) copy.calculateCardDamage(target);
        else copy.applyPowers();
        AbstractDungeon.player.limbo.addToBottom(copy);
        AbstractDungeon.actionManager.addCardQueueItem(
                new CardQueueItem(copy, target, copy.energyOnUse, true, true), true);
    }
}
