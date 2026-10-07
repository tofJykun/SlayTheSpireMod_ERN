package powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FadeIntoShadowPower extends AbstractPower {
    public static final String POWER_ID = "FadeIntoShadowPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean expired;

    public FadeIntoShadowPower(AbstractCreature owner, int amount) {
        ID = POWER_ID;
        name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        isTurnBased = true;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    public float amplifyBlock(float block) {
        if (expired || amount <= 0 || block <= 0.0F) {
            return block;
        }
        flash();
        // Preserve the native block cap without allowing large stacks to overflow.
        return (float)Math.min(999.0D, Math.scalb((double)block, amount));
    }

    @Override
    public void stackPower(int stackAmount) {
        amount = (int)Math.max(0L, Math.min(Integer.MAX_VALUE, (long)amount + stackAmount));
        fontScale = 8.0F;
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        expire();
    }

    public void expire() {
        if (expired) {
            return;
        }
        expired = true;
        addToTop(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0] + amount + STRINGS.DESCRIPTIONS[1];
    }
}
