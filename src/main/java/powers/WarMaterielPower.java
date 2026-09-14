package powers;

import actions.WarMaterielAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class WarMaterielPower extends AbstractPower {
    public static final String POWER_ID = "WarMaterielPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    public WarMaterielPower(AbstractCreature owner, int cardsToChoose) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = cardsToChoose;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot((AbstractGameAction)new GainEnergyAction(1));
        addToBot((AbstractGameAction)new WarMaterielAction((com.megacrit.cardcrawl.characters.AbstractPlayer)this.owner, this.amount));
        addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
