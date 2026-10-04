package powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RallyingStandardPower extends AbstractPower {
    public static final String POWER_ID = "RallyingStandardPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final AbstractSummonPower summon;

    public RallyingStandardPower(AbstractCreature owner, AbstractSummonPower summon) {
        this.ID = POWER_ID + ":" + summon.ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = 1;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        this.summon = summon;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        fontScale = 8.0F;
        amount += stackAmount;
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (amount > 0) {
            flash();
            for (int i = 0; i < amount; i++) {
                addToBot(new ApplyPowerAction(owner, owner, summon.makeSummonCopy(owner), 1));
            }
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0],
                "#y" + summon.name.replace(" ", " #y"), amount);
    }
}
