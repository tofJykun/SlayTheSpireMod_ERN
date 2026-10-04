package actions;

import cards.guardian.BastardSword;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class BastardSwordAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final DamageInfo info;
    private final boolean upgraded;
    private boolean firstFrame = true;

    public BastardSwordAction(AbstractPlayer player, AbstractMonster target, DamageInfo info, boolean upgraded) {
        this.player = player;
        this.info = info;
        this.upgraded = upgraded;
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
            if ((target.isDying || target.currentHealth <= 0)
                    && !target.halfDead && !target.hasPower("Minion")) {
                BastardSword reward = new BastardSword();
                if (upgraded) {
                    reward.upgrade();
                }
                // Preserve the native obtain flow even if the final kill clears queued actions.
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(
                        reward, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            }
        }
        tickDuration();
    }
}
