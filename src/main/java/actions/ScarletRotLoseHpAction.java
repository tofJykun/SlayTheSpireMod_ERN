package actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import general.CombatState;
import powers.ScarletRotPower;

public class ScarletRotLoseHpAction extends AbstractGameAction {
    private static final float DURATION = 0.33F;

    public ScarletRotLoseHpAction(AbstractCreature target, AbstractCreature source, AttackEffect effect) {
        setValues(target, source);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = DURATION;
    }

    @Override
    public void update() {
        AbstractRoom room = CombatState.currentRoom();
        if (room == null || room.phase != AbstractRoom.RoomPhase.COMBAT) {
            this.isDone = true;
            return;
        }

        AbstractPower power = this.target.getPower(ScarletRotPower.POWER_ID);
        if (power == null || power.amount <= 0) {
            this.isDone = true;
            return;
        }

        if (this.duration == DURATION && this.target.currentHealth > 0) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY,
                    this.attackEffect));
        }

        tickDuration();

        if (this.isDone) {
            if (this.target.currentHealth > 0) {
                int damage = power.amount;
                this.target.tint.color = Color.RED.cpy();
                this.target.tint.changeColor(Color.WHITE.cpy());
                this.target.damage(new DamageInfo(this.source, damage, DamageInfo.DamageType.HP_LOSS));
            }

            power.amount--;
            if (power.amount <= 0) {
                this.target.powers.remove(power);
            } else {
                power.updateDescription();
            }

            if (room.monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
            addToTop((AbstractGameAction)new WaitAction(0.1F));
        }
    }
}
