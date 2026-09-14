package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DeadOrAliveAction extends AbstractGameAction {
    private final AbstractPlayer player;

    public DeadOrAliveAction(AbstractPlayer player, AbstractCreature target) {
        this.player = player;
        this.target = target;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST
                && this.player != null
                && this.target instanceof AbstractMonster
                && !this.target.isDeadOrEscaped()
                && this.target.currentHealth > 0
                && this.player.gold >= this.target.currentHealth) {
            int goldCost = this.target.currentHealth;
            this.player.loseGold(goldCost);
            AbstractDungeon.actionManager.addToTop(new InstantKillAction(this.target));
        }
        this.isDone = true;
    }
}
