package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import java.util.ArrayList;

public class PolebladeAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final AbstractCard sourceCard;
    private final int[] damage;
    private boolean firstFrame = true;

    public PolebladeAction(AbstractPlayer player, AbstractCard card, int[] damage, DamageInfo.DamageType type) {
        this.player = player;
        this.source = player;
        this.sourceCard = card.makeStatEquivalentCopy();
        this.damage = damage.clone();
        this.damageType = type;
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = AttackEffect.SLASH_HORIZONTAL;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    private static boolean canHit(AbstractMonster monster) {
        return !monster.isDeadOrEscaped() && !monster.isDying && !monster.halfDead
                && monster.currentHealth > 0;
    }

    @Override
    public void update() {
        if (this.isDone) {
            return;
        }
        if (this.player == null || this.player.isDying) {
            this.isDone = true;
            return;
        }
        if (this.firstFrame) {
            boolean silent = false;
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (canHit(monster)) {
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(
                            monster.hb.cX, monster.hb.cY, this.attackEffect, silent));
                    silent = true;
                }
            }
            this.firstFrame = false;
        }
        tickDuration();
        if (!this.isDone) {
            return;
        }
        for (AbstractPower power : this.player.powers) {
            power.onDamageAllEnemies(this.damage);
        }
        boolean killed = false;
        ArrayList<AbstractMonster> monsters = new ArrayList<>(AbstractDungeon.getMonsters().monsters);
        for (int i = 0; i < monsters.size() && i < this.damage.length; i++) {
            AbstractMonster monster = monsters.get(i);
            if (!canHit(monster)) {
                continue;
            }
            monster.damage(new DamageInfo(this.player, this.damage[i], this.damageType));
            // Check immediately after our own hit, including minions, as with Sunder.
            killed |= monster.isDying || monster.currentHealth <= 0;
        }
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
            return;
        }
        if (killed && !this.player.isDying) {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (canHit(monster)) {
                    AbstractCard copy = this.sourceCard.makeSameInstanceOf();
                    copy.purgeOnUse = true;
                    copy.freeToPlayOnce = true;
                    // Purged copies still run use(), so subsequent kills can repeat again.
                    addToBot(new NewQueueCardAction(copy, null, true, true));
                    break;
                }
            }
        }
    }
}
