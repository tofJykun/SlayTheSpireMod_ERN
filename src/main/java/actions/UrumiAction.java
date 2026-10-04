package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import java.util.ArrayList;

public class UrumiAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int[] damage;
    private final int draw;
    private boolean firstFrame = true;

    public UrumiAction(AbstractPlayer player, int[] damage, DamageInfo.DamageType type, int draw) {
        this.player = player;
        this.source = player;
        this.damage = damage.clone();
        this.draw = draw;
        this.damageType = type;
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = AttackEffect.SLASH_DIAGONAL;
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
            if (canHit(monster)) {
                monster.damage(new UrumiDamageInfo(player, damage[i], damageType, draw));
            }
        }
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
        }
    }

    public static class UrumiDamageInfo extends DamageInfo {
        private final int draw;
        private boolean reported;

        public UrumiDamageInfo(AbstractCreature owner, int damage, DamageType type, int draw) {
            super(owner, damage, type);
            this.draw = draw;
        }

        public void onAttack(int damageAmount, AbstractCreature target) {
            // Same positive NORMAL damage test as Envenom, after block and damage prevention.
            if (!reported && damageAmount > 0 && target != owner && type == DamageType.NORMAL) {
                AbstractDungeon.actionManager.addToTop(new DrawCardAction(draw));
            }
            reported = true;
        }
    }
}
