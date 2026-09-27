package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import java.util.ArrayList;

public class BlasphemousBladeAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int[] damage;
    private final int heal;
    private boolean firstFrame = true;

    public BlasphemousBladeAction(AbstractPlayer player, int[] damage, DamageInfo.DamageType type, int heal) {
        this.player = player;
        this.source = player;
        this.damage = damage.clone();
        this.heal = heal;
        this.damageType = type;
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = AttackEffect.FIRE;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    private static boolean canHit(AbstractMonster monster) {
        return !monster.isDeadOrEscaped() && !monster.isDying && !monster.halfDead
                && monster.currentHealth > 0;
    }

    @Override
    public void update() {
        if (firstFrame) {
            boolean silent = false;
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (canHit(monster)) {
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(
                            monster.hb.cX, monster.hb.cY, attackEffect, silent));
                    silent = true;
                }
            }
            firstFrame = false;
        }
        tickDuration();
        if (!isDone) {
            return;
        }
        for (AbstractPower power : player.powers) {
            power.onDamageAllEnemies(damage);
        }
        ArrayList<AbstractMonster> monsters = new ArrayList<>(AbstractDungeon.getMonsters().monsters);
        for (int i = 0; i < monsters.size() && i < damage.length; i++) {
            AbstractMonster monster = monsters.get(i);
            if (!canHit(monster)) {
                continue;
            }
            monster.damage(new DamageInfo(player, damage[i], damageType));
            if ((monster.isDying || monster.currentHealth <= 0)
                    && !monster.halfDead && !monster.hasPower("Minion")) {
                // Heal before combat cleanup can discard queued actions after the final kill.
                player.heal(heal);
            }
        }
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
        }
    }
}
