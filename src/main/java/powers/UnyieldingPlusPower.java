package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class UnyieldingPlusPower extends AbstractPower {
    public static final String POWER_ID = "UnyieldingPlusPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public UnyieldingPlusPower(AbstractCreature owner, int amount) {
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
    public int onLoseHp(int damageAmount) {
        if (UnyieldingPower.bypassGreyHealth || damageAmount <= 0) {
            return damageAmount;
        }

        flash();
        addToTop((AbstractGameAction)new ApplyPowerAction(this.owner, this.owner,
                new GreyHealthPlusPower(this.owner, damageAmount), damageAmount));
        return 0;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}

