package powers;

import actions.GreyHealthPlusSettleAction;
import actions.GreyHealthLoseHpAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import relics.BorrowedLife;

public class GreyHealthPlusPower extends AbstractPower {
    public static final String POWER_ID = "GreyHealthPlusPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int ATTACK_REDUCTION = 2;

    public GreyHealthPlusPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.canGoNegative = false;
        loadRegion("painfulStabs");
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            addToTop((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            updateDescription();
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK && this.amount > 0) {
            flash();
            if (this.amount <= ATTACK_REDUCTION) {
                addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
            } else {
                addToBot((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, this, ATTACK_REDUCTION));
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && this.amount > 0) {
            addToBot((AbstractGameAction)new GreyHealthPlusSettleAction(this.owner));
        }
    }

    @Override
    public void onVictory() {
        if (this.amount > 0) {
            flash();
            if (AbstractDungeon.player == null || !AbstractDungeon.player.hasRelic(BorrowedLife.ID)) {
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
