package powers;

import actions.GreyHealthSettleAction;
import actions.GreyHealthLoseHpAction;
import actions.BlueTearstoneRingAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.GreyHealthAttackReduction;
import relics.BorrowedLife;

public class GreyHealthPower extends AbstractPower {
    public static final String POWER_ID = "GreyHealthPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private final GreyHealthAttackReduction attackReduction = new GreyHealthAttackReduction(this);

    public GreyHealthPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            addToTop((AbstractGameAction)new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(
                    this.owner, this.owner, this));
        } else {
            updateDescription();
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK && this.amount > 0) {
            flash();
            this.attackReduction.queue(1);
        }
    }

    public void settlePendingAttackReductions() {
        this.attackReduction.settlePending();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && this.amount > 0) {
            addToBot((AbstractGameAction)new GreyHealthSettleAction(this.owner));
        }
    }

    @Override
    public void onVictory() {
        GreyHealthAttackReduction.settleBeforeVictory(this.owner);
        if (this.amount > 0) {
            flash();
            if (AbstractDungeon.player == null || !AbstractDungeon.player.hasRelic(BorrowedLife.ID)) {
                BlueTearstoneRingAction.triggerAtCombatEnd(this.owner);
                try {
                    UnyieldingPower.bypassGreyHealth = true;
                    GreyHealthLoseHpAction.loseHpDirectly(this.owner, this.amount);
                } finally {
                    UnyieldingPower.bypassGreyHealth = false;
                }
            }
            this.amount = 0;
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}

