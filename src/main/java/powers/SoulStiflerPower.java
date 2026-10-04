package powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SoulStiflerPower extends AbstractPower {
    public static final String POWER_ID = "SoulStiflerPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public SoulStiflerPower(AbstractCreature owner) {
        ID = POWER_ID;
        name = STRINGS.NAME;
        this.owner = owner;
        amount = -1;
        type = PowerType.DEBUFF;
        isTurnBased = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        owner.loseBlock();
    }

    @Override
    public void stackPower(int stackAmount) {
        owner.loseBlock();
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0];
    }
}
