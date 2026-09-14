package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class LoyaltyCardPower extends AbstractPower {
    public static final String POWER_ID = "LoyaltyCardPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public LoyaltyCardPower(AbstractCreature owner) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 1;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    public void onTokensLost(int amountLost) {
        if (amountLost <= 0) {
            return;
        }
        flash();
        addToBot((AbstractGameAction)new GainBlockAction(this.owner, this.owner, amountLost));
    }

    @Override
    public void stackPower(int stackAmount) {
        flash();
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
