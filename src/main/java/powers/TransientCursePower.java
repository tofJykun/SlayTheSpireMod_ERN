package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class TransientCursePower extends AbstractPower {
    public static final String POWER_ID = "TransientCursePower";
    private static final int DEXTERITY_RETURN_PER_APPLICATION = 3;
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private int dexterityReturn;

    public TransientCursePower(AbstractCreature owner, int dexterityReturn, int strengthLoss) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = strengthLoss;
        this.dexterityReturn = dexterityReturn;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        loadRegion("flex");
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        this.dexterityReturn += DEXTERITY_RETURN_PER_APPLICATION;
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            flash();
            addToBot((AbstractGameAction)new ApplyPowerAction(this.owner, this.owner,
                    new DexterityPower(this.owner, this.dexterityReturn), this.dexterityReturn));
            addToBot((AbstractGameAction)new ApplyPowerAction(this.owner, this.owner,
                    new StrengthPower(this.owner, -this.amount), -this.amount));
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.dexterityReturn + DESCRIPTIONS[1]
                + this.amount + DESCRIPTIONS[2];
    }
}
