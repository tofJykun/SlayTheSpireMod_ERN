package powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;

public class VordtGreatHammerPower extends AbstractPower {
    public static final String POWER_ID = "VordtGreatHammerPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean active = true;

    public VordtGreatHammerPower(AbstractCreature owner, int energyPerHit) {
        this.name = POWER_STRINGS.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = energyPerHit;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        // Recorded after Block/relic mitigation, before Unyielding converts the loss to Grey Health.
        if (this.active && info != null && info.type == DamageInfo.DamageType.NORMAL
                && this.owner.lastDamageTaken > 0 && this.amount > 0) {
            flash();
            addToBot(new ApplyPowerAction(this.owner, this.owner,
                    new EnergizedPower(this.owner, this.amount), this.amount));
        }
    }

    @Override
    public void atStartOfTurn() {
        this.active = false;
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = POWER_STRINGS.DESCRIPTIONS[0] + this.amount + POWER_STRINGS.DESCRIPTIONS[1];
    }
}
