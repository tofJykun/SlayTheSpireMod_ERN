package powers;

import actions.CatarinaRoundMoonAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class CatarinaRoundMoonPower extends AbstractPower {
    public static final String POWER_ID = "CatarinaRoundMoonPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static int powerIdOffset;
    private boolean triggered;

    public CatarinaRoundMoonPower(AbstractCreature owner) {
        this.name = POWER_STRINGS.NAME;
        this.ID = POWER_ID + powerIdOffset++;
        this.owner = owner;
        this.amount = 1;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (this.triggered) {
            return;
        }
        this.triggered = true;
        flash();
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        addToBot(new CatarinaRoundMoonAction(this.owner));
    }

    @Override
    public void updateDescription() {
        this.description = POWER_STRINGS.DESCRIPTIONS[0];
    }
}
