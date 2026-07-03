package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LetFeastBeginAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int maxHpGain;

    public LetFeastBeginAction(AbstractPlayer player, AbstractMonster target, int maxHpGain) {
        this.player = player;
        this.target = target;
        this.maxHpGain = maxHpGain;
    }

    @Override
    public void update() {
        if (this.player == null || this.target == null) {
            this.isDone = true;
            return;
        }

        if (this.target instanceof AbstractMonster
                && this.target.maxHealth <= this.player.maxHealth
                && !this.target.isDying
                && !this.target.isDeadOrEscaped()) {
            boolean shouldGainMaxHp = !this.target.halfDead && !this.target.hasPower("Minion");
            addToTop(new InstantKillAction(this.target));
            if (shouldGainMaxHp) {
                this.player.increaseMaxHp(this.maxHpGain, false);
            }
        }
        this.isDone = true;
    }
}
