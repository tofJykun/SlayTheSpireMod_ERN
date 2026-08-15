package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class MendicantStaffGoldAction extends AbstractGameAction {
    private static final float DURATION = 0.1F;
    private final DamageInfo info;

    public MendicantStaffGoldAction(AbstractCreature target, DamageInfo info, AttackEffect effect) {
        this.info = info;
        setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = DURATION;
    }

    @Override
    public void update() {
        if (this.duration == DURATION && this.target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(
                    this.target.hb.cX, this.target.hb.cY, this.attackEffect));
            this.target.damage(this.info);

            int goldAmount = Math.max(0, this.target.lastDamageTaken);
            if (goldAmount > 0) {
                AbstractDungeon.player.gainGold(goldAmount);
                for (int i = 0; i < goldAmount; i++) {
                    AbstractDungeon.effectList.add(new GainPennyEffect(this.source,
                            this.target.hb.cX, this.target.hb.cY,
                            this.source.hb.cX, this.source.hb.cY, true));
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        tickDuration();
    }
}
