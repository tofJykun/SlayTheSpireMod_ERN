package actions;

import cards.tempcards.LotteryTicket;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class DeathLightningAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final DamageInfo info;
    private final int tickets;
    private boolean firstFrame = true;

    public DeathLightningAction(AbstractPlayer player, AbstractMonster target, DamageInfo info, int tickets) {
        this.player = player;
        this.info = info;
        this.tickets = tickets;
        setValues(target, info);
        actionType = ActionType.DAMAGE;
        attackEffect = AttackEffect.LIGHTNING;
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
                LotteryTicket ticket = new LotteryTicket();
                ticket.upgrade();
                addToTop(new MakeTempCardInHandAction(ticket, tickets));
            }
        }
        tickDuration();
    }
}
