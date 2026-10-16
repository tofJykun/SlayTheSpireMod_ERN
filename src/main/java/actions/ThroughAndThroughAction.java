package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import powers.VirtualCurrencyPower;
import java.util.ArrayList;

public class ThroughAndThroughAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int[] damage;

    public ThroughAndThroughAction(AbstractPlayer player, int[] damage, DamageInfo.DamageType type) {
        this.player = player;
        this.source = player;
        this.damage = damage.clone();
        this.damageType = type;
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (isDone) return;
        tickDuration();
        if (!isDone) return;
        for (AbstractPower power : new ArrayList<>(player.powers)) {
            power.onDamageAllEnemies(damage);
        }
        ArrayList<AbstractMonster> monsters = new ArrayList<>(AbstractDungeon.getMonsters().monsters);
        long total = 0;
        for (int i = 0; i < monsters.size() && i < damage.length; i++) {
            AbstractMonster monster = monsters.get(i);
            if (monster.isDeadOrEscaped() || monster.currentHealth <= 0) continue;
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(monster.hb.cX, monster.hb.cY,
                    AttackEffect.SLASH_HORIZONTAL));
            monster.damage(new DamageInfo(player, damage[i], damageType));
            // Snapshot each hit immediately, using the same result as Wallop.
            total += Math.max(0, monster.lastDamageTaken);
        }
        if (total > 0) {
            int tokens = (int)Math.min(Integer.MAX_VALUE, total);
            addToBot(new ApplyPowerAction(player, player, new VirtualCurrencyPower(player, tokens), tokens));
        }
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
        }
    }
}
