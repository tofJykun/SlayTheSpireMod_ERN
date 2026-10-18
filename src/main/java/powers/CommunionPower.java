package powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class CommunionPower extends AbstractPower {
    public static final String POWER_ID = "CommunionPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public CommunionPower(AbstractCreature owner) {
        ID = POWER_ID;
        name = STRINGS.NAME;
        this.owner = owner;
        amount = -1;
        type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        amount = -1;
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0];
    }
}
