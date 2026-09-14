package powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class VirtualCurrencyPower extends AbstractPower {
    public static final String POWER_ID = "VirtualCurrencyPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public VirtualCurrencyPower(AbstractCreature owner, int amount) {
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
    public void reducePower(int reduceAmount) {
        loseTokens(reduceAmount);
    }

    public int loseTokens(int requestedAmount) {
        if (requestedAmount <= 0 || this.amount <= 0) {
            return 0;
        }

        int amountLost = Math.min(this.amount, requestedAmount);
        this.fontScale = 8.0F;
        this.amount -= amountLost;
        if (this.amount <= 0) {
            this.onRemove();
            this.owner.powers.remove(this);
            AbstractDungeon.onModifyPower();
        } else {
            updateDescription();
        }

        AbstractPower loyalty = this.owner.getPower(LoyaltyCardPower.POWER_ID);
        if (loyalty instanceof LoyaltyCardPower) {
            ((LoyaltyCardPower)loyalty).onTokensLost(amountLost);
        }
        return amountLost;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
