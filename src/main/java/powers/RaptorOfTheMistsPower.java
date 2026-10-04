package powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RaptorOfTheMistsPower extends AbstractPower {
    public static final String POWER_ID = "RaptorOfTheMistsPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean expired;

    public RaptorOfTheMistsPower(AbstractCreature owner, int amount) {
        ID = POWER_ID;
        name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        isTurnBased = true;
        // Powers run in ascending priority; vanilla Buffer has priority 5.
        priority = 4;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (expired || amount <= 0 || damageAmount <= 0) {
            return damageAmount;
        }
        flash();
        // Spend immediately so multiple damage calls in one action cannot reuse a charge.
        reducePower(1);
        updateDescription();
        if (amount <= 0) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
        return 0;
    }

    @Override
    public void atStartOfTurn() {
        expired = true;
        addToTop(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0] + amount + STRINGS.DESCRIPTIONS[1];
    }
}
