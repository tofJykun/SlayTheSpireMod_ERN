package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Arrays;
import java.util.List;

public class SoulAppeaseAction extends AbstractGameAction {
    private final AbstractCreature source;
    private final int[] damage;
    private final DamageInfo.DamageType damageType;
    private final int healAmount;

    public SoulAppeaseAction(AbstractCreature source, int[] damage, DamageInfo.DamageType damageType, int healAmount) {
        this.source = source;
        this.damage = Arrays.copyOf(damage, damage.length);
        this.damageType = damageType;
        this.healAmount = healAmount;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        List<AbstractMonster> monsters = AbstractDungeon.getMonsters().monsters;
        int count = Math.min(monsters.size(), this.damage.length);
        for (int i = count - 1; i >= 0; i--) {
            AbstractMonster monster = monsters.get(i);
            if (monster.isDeadOrEscaped()) {
                continue;
            }
            int halfHealthRoundedUp = (monster.maxHealth + 1) / 2;
            if (monster.currentHealth <= halfHealthRoundedUp) {
                addToTop((AbstractGameAction)new DamageAction((AbstractCreature)monster,
                        new DamageInfo(this.source, this.damage[i], this.damageType),
                        AttackEffect.FIRE));
            } else {
                addToTop((AbstractGameAction)new HealAction((AbstractCreature)monster,
                        (AbstractCreature)monster, this.healAmount));
            }
        }
        this.isDone = true;
    }
}
