package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import powers.LessLikelyToBeTargetedPower;

public class ReplaceAttackWithBlockAction extends AbstractGameAction {
    private final AbstractMonster monster;

    public ReplaceAttackWithBlockAction(AbstractMonster monster, AbstractCreature source, int block) {
        this.monster = monster;
        this.source = source;
        this.amount = block;
        this.actionType = ActionType.POWER;
    }

    @Override
    public void update() {
        isDone = true;
        if (monster != null && !monster.isDeadOrEscaped() && !monster.halfDead
                && LessLikelyToBeTargetedPower.isAttackIntent(monster.intent)) {
            addToTop(new ApplyPowerAction(monster, source,
                    new LessLikelyToBeTargetedPower(monster, amount), amount));
        }
    }
}
