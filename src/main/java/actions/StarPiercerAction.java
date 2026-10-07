package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import general.SmithingBody;

public class StarPiercerAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final AbstractCard card;
    private final DamageInfo info;
    private final int vigor;
    private boolean firstFrame = true;

    public StarPiercerAction(AbstractPlayer player, AbstractMonster monster, AbstractCard card,
                             DamageInfo info, int vigor) {
        this.player = player;
        this.card = SmithingBody.physical(card);
        this.info = info;
        this.vigor = vigor;
        setValues(monster, info);
        actionType = ActionType.DAMAGE;
        attackEffect = AttackEffect.SLASH_HEAVY;
        duration = 0.1F;
    }

    @Override
    public void update() {
        if (isDone) return;
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
                // Wait for Vigor removal and UseCardAction to finish before refunding and returning.
                addToBot(new GainEnergyAction(3));
                addToBot(new OneTwoPunchReturnAction(player, card));
                if (vigor > 0) {
                    addToBot(new ApplyPowerAction(player, player, new VigorPower(player, vigor), vigor));
                }
            }
        }
        tickDuration();
    }
}
