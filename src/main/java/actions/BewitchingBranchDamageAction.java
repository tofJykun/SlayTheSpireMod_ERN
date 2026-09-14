package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BewitchingBranchDamageAction extends AbstractGameAction {
    private final AbstractCard sourceCard;
    private final DamageInfo info;

    public BewitchingBranchDamageAction(AbstractCard sourceCard, DamageInfo info) {
        this.sourceCard = sourceCard == null ? null : sourceCard.makeStatEquivalentCopy();
        this.info = info;
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = AttackEffect.SLASH_HORIZONTAL;
    }

    @Override
    public void update() {
        if (this.sourceCard == null || this.info == null || AbstractDungeon.getMonsters() == null) {
            this.isDone = true;
            return;
        }

        AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        if (target == null) {
            this.isDone = true;
            return;
        }

        int livingEnemiesBeforeHit = countLivingEnemies();
        addToTop(new BewitchingBranchRepeatCheckAction(this.sourceCard, target, livingEnemiesBeforeHit));
        addToTop(new DamageAction(target, this.info, this.attackEffect));
        this.isDone = true;
    }

    private static int countLivingEnemies() {
        int count = 0;
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster != null && !monster.isDeadOrEscaped() && !monster.isDying && !monster.halfDead) {
                count++;
            }
        }
        return count;
    }

    private static class BewitchingBranchRepeatCheckAction extends AbstractGameAction {
        private final AbstractCard sourceCard;
        private final AbstractMonster targetMonster;
        private final int livingEnemiesBeforeHit;

        private BewitchingBranchRepeatCheckAction(AbstractCard sourceCard, AbstractMonster targetMonster,
                                                 int livingEnemiesBeforeHit) {
            this.sourceCard = sourceCard == null ? null : sourceCard.makeStatEquivalentCopy();
            this.targetMonster = targetMonster;
            this.livingEnemiesBeforeHit = livingEnemiesBeforeHit;
        }

        @Override
        public void update() {
            if (this.sourceCard == null || this.targetMonster == null || this.livingEnemiesBeforeHit < 3
                    || killedTarget() || reachedCardPlayLimit() || AbstractDungeon.player == null
                    || AbstractDungeon.getMonsters() == null || countLivingEnemies() <= 0) {
                this.isDone = true;
                return;
            }

            AbstractCard copy = this.sourceCard.makeSameInstanceOf();
            copy.purgeOnUse = true;
            copy.freeToPlayOnce = true;
            copy.current_x = this.sourceCard.current_x;
            copy.current_y = this.sourceCard.current_y;
            copy.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            copy.target_y = Settings.HEIGHT / 2.0F;
            copy.applyPowers();

            AbstractDungeon.player.limbo.addToBottom(copy);
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(copy, null, 0, true, true), true);
            this.isDone = true;
        }

        private boolean killedTarget() {
            return this.targetMonster.currentHealth <= 0 || this.targetMonster.isDying
                    || this.targetMonster.isDeadOrEscaped() || this.targetMonster.halfDead;
        }

        private boolean reachedCardPlayLimit() {
            return AbstractDungeon.actionManager.cardsPlayedThisTurn != null
                    && AbstractDungeon.actionManager.cardsPlayedThisTurn.size() >= 999;
        }
    }
}
