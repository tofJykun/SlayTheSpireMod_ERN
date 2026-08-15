package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.DeadEnemyStats;

public class DeadAgainAction extends AbstractGameAction {
    private final AbstractCreature source;

    public DeadAgainAction(AbstractCreature source) {
        this.source = source;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        int damage = DeadEnemyStats.getDeadEnemyMaxHealthSum();
        if (damage > 0) {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (!monster.isDeadOrEscaped() && !monster.isDying && !monster.halfDead) {
                    addToTop(new DamageAction(monster,
                            new DamageInfo(this.source, damage, DamageInfo.DamageType.THORNS),
                            AttackEffect.BLUNT_HEAVY));
                }
            }
        }
        this.isDone = true;
    }
}
