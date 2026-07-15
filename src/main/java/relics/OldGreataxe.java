package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import powers.PoiseBreakPower;

import java.util.LinkedHashSet;
import java.util.Set;

public class OldGreataxe extends CustomRelic {
    public static final String ID = "OldGreataxe";
    private static final String IMG = "img/relics/raider/OldGreataxe.png";
    private static final String IMG_OTL = "img/relics/raider/outline/OldGreataxe.png";
    private static final int MIN_COST = 2;
    private static final int POISE_BREAK = 1;
    private static TrackingSession activeSession;

    public OldGreataxe() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.COMMON, AbstractRelic.LandingSound.HEAVY);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card == null || card.type != AbstractCard.CardType.ATTACK || !costsEnough(card)) {
            return;
        }

        activeSession = new TrackingSession(this, card);
    }

    private boolean costsEnough(AbstractCard card) {
        return card.costForTurn >= MIN_COST || (card.cost == -1 && card.energyOnUse >= MIN_COST);
    }

    public static void recordAttackedEnemy(AbstractMonster monster, DamageInfo info) {
        if (activeSession == null || monster == null || info == null || info.owner == null
                || !info.owner.isPlayer || info.type != DamageInfo.DamageType.NORMAL
                || monster.isDeadOrEscaped()) {
            return;
        }

        activeSession.targets.add(monster);
    }

    public static void queueFinish(AbstractCard card) {
        if (activeSession == null || activeSession.card != card || activeSession.finishQueued) {
            return;
        }
        activeSession.finishQueued = true;
        AbstractDungeon.actionManager.addToBottom(new FinishTrackingAction(activeSession));
    }

    private void applyToMonster(AbstractMonster monster) {
        if (monster == null || monster.isDeadOrEscaped()) {
            return;
        }

        flash();
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new RelicAboveCreatureAction(
                (AbstractCreature)monster, this));
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction(
                (AbstractCreature)monster, (AbstractCreature)AbstractDungeon.player,
                new PoiseBreakPower((AbstractCreature)monster, POISE_BREAK), POISE_BREAK));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new OldGreataxe();
    }

    private static class TrackingSession {
        private final OldGreataxe relic;
        private final AbstractCard card;
        private final Set<AbstractMonster> targets = new LinkedHashSet<>();
        private boolean finishQueued;

        private TrackingSession(OldGreataxe relic, AbstractCard card) {
            this.relic = relic;
            this.card = card;
        }
    }

    private static class FinishTrackingAction extends AbstractGameAction {
        private final TrackingSession session;

        private FinishTrackingAction(TrackingSession session) {
            this.session = session;
        }

        @Override
        public void update() {
            if (activeSession == this.session) {
                activeSession = null;
            }
            if (!this.session.targets.isEmpty()) {
                this.session.relic.flash();
                for (AbstractMonster monster : this.session.targets) {
                    this.session.relic.applyToMonster(monster);
                }
            }
            this.isDone = true;
        }
    }
}
