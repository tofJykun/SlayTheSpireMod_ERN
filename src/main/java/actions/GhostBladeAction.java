package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import powers.ArcanePower;

public class GhostBladeAction extends AbstractGameAction {
    private static final float DURATION = 0.1F;
    private final DamageInfo info;
    private final int arcaneGain;

    public GhostBladeAction(AbstractCreature target, DamageInfo info, int arcaneGain) {
        this.info = info;
        this.arcaneGain = arcaneGain;
        setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = AttackEffect.SLASH_HORIZONTAL;
        this.duration = DURATION;
    }

    @Override
    public void update() {
        if (this.duration == DURATION && this.target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(
                    this.target.hb.cX, this.target.hb.cY, this.attackEffect));
            this.target.damage(this.info);

            if ((this.target.isDying || this.target.currentHealth <= 0)
                    && !this.target.halfDead && !this.target.hasPower("Minion")) {
                addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new ArcanePower(AbstractDungeon.player, this.arcaneGain), this.arcaneGain));
            }
        }
        tickDuration();
    }
}
