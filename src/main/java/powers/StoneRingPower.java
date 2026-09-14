package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.LinkedHashSet;
import java.util.Set;

public class StoneRingPower extends AbstractPower {
    public static final String POWER_ID = "StoneRingPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private static final int ATTACKS_PER_TRIGGER = 3;
    private static TrackingSession activeSession;

    private int attacksThisTurn;

    public StoneRingPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        this.attacksThisTurn = 0;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.attacksThisTurn = 0;
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card == null || card.type != AbstractCard.CardType.ATTACK || this.amount <= 0) {
            return;
        }

        this.attacksThisTurn++;
        if (this.attacksThisTurn % ATTACKS_PER_TRIGGER == 0) {
            activeSession = new TrackingSession(this, card, this.amount);
        }
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

    private void applyToMonster(AbstractMonster monster, int poiseBreak) {
        if (monster == null || monster.isDeadOrEscaped() || poiseBreak <= 0) {
            return;
        }

        flash();
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction(
                (AbstractCreature)monster, (AbstractCreature)AbstractDungeon.player,
                new PoiseBreakPower((AbstractCreature)monster, poiseBreak), poiseBreak));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    private static class TrackingSession {
        private final StoneRingPower power;
        private final AbstractCard card;
        private final int poiseBreak;
        private final Set<AbstractMonster> targets = new LinkedHashSet<>();
        private boolean finishQueued;

        private TrackingSession(StoneRingPower power, AbstractCard card, int poiseBreak) {
            this.power = power;
            this.card = card;
            this.poiseBreak = poiseBreak;
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
                this.session.power.flash();
                for (AbstractMonster monster : this.session.targets) {
                    this.session.power.applyToMonster(monster, this.session.poiseBreak);
                }
            }
            this.isDone = true;
        }
    }
}
