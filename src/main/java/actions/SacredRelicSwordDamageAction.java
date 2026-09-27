package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import java.util.ArrayList;

public class SacredRelicSwordDamageAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int[] damage;
    private final int gold;
    private boolean firstFrame = true;

    public SacredRelicSwordDamageAction(AbstractPlayer player, int[] damage,
                                        DamageInfo.DamageType type, int gold) {
        this.player = player;
        this.source = player;
        this.damage = damage.clone();
        this.gold = gold;
        this.damageType = type;
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = AttackEffect.SLASH_HEAVY;
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
            // Pay in this action: a lethal hit can clear queued post-combat rewards.
            player.gainGold(gold);
            for (int penny = 0; penny < gold; penny++) {
                AbstractDungeon.effectList.add(new GainPennyEffect(player, monster.hb.cX,
                        monster.hb.cY, player.hb.cX, player.hb.cY, true));
            }
        }
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
        }
    }
}
