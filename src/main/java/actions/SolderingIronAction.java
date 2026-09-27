package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class SolderingIronAction extends AbstractGameAction {
    private final AbstractMonster monster;
    private boolean firstFrame = true;

    public SolderingIronAction(AbstractMonster monster, AbstractCreature source, int amount) {
        this.monster = monster;
        setValues(monster, source, amount);
        actionType = ActionType.DAMAGE;
        attackEffect = AttackEffect.FIRE;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (isDone) {
            return;
        }
        if (monster == null || monster.isDeadOrEscaped() || monster.isDying
                || monster.halfDead || monster.currentHealth <= 0 || amount <= 0) {
            isDone = true;
            return;
        }
        if (firstFrame) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(monster.hb.cX, monster.hb.cY, attackEffect));
            firstFrame = false;
        }
        tickDuration();
        if (!isDone) {
            return;
        }

        monster.damage(new DamageInfo(source, amount, DamageInfo.DamageType.HP_LOSS));
        // Complete both losses in this action, even when the first one ends combat.
        if (monster.maxHealth > amount) {
            monster.decreaseMaxHealth(amount);
        } else {
            // Keep the engine's positive max-HP invariant for rendering and death hooks.
            monster.maxHealth = 1;
            monster.currentHealth = 0;
            monster.halfDead = false;
            monster.healthBarUpdatedEvent();
            if (monster instanceof AwakenedOne) {
                AbstractDungeon.getCurrRoom().cannotLose = false;
            }
            monster.die(true);
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                AbstractDungeon.getCurrRoom().cannotLose = false;
            }
        }
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
        }
    }
}
