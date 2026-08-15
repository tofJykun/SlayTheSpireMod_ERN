package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import cards.recluse.TheGnawling;

import java.util.UUID;

public class TheGnawlingAction extends AbstractGameAction {
    private static final float DURATION = 0.1F;
    private final DamageInfo info;
    private final UUID cardUuid;

    public TheGnawlingAction(AbstractCreature target, DamageInfo info, UUID cardUuid) {
        this.info = info;
        this.cardUuid = cardUuid;
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
                increasePermanentIntelligence();
            }
        }
        tickDuration();
    }

    private void increasePermanentIntelligence() {
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card.uuid.equals(this.cardUuid)) {
                increaseCard(card);
            }
        }
        for (AbstractCard card : GetAllInBattleInstances.get(this.cardUuid)) {
            increaseCard(card);
        }
    }

    private static void increaseCard(AbstractCard card) {
        card.misc++;
        if (card instanceof TheGnawling) {
            ((TheGnawling)card).refreshPermanentIntelligenceDisplay();
        } else {
            card.applyPowers();
        }
    }
}
