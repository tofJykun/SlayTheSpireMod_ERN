package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BerserkPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class SwordOfMilosAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final DamageInfo info;
    private boolean firstFrame = true;

    public SwordOfMilosAction(AbstractPlayer player, AbstractMonster target, DamageInfo info) {
        this.player = player;
        this.info = info;
        setValues(target, info);
        actionType = ActionType.DAMAGE;
        attackEffect = AttackEffect.SLASH_HEAVY;
        duration = 0.1F;
    }

    @Override
    public void update() {
        if (firstFrame) {
            firstFrame = false;
            if (player == null || player.isDying || target == null || target.isDeadOrEscaped()
                    || target.isDying || target.halfDead || target.currentHealth <= 0) {
                isDone = true;
                return;
            }
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, attackEffect));
            target.damage(info);
            if (target.isDying || target.currentHealth <= 0) {
                addToTop(new ApplyPowerAction(player, player, new BerserkPower(player, 1), 1));
            }
        }
        tickDuration();
    }
}
