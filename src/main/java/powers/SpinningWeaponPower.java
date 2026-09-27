package powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SpinningWeaponPower extends AbstractPower {
    public static final String POWER_ID = "SpinningWeaponPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean triggered;

    public SpinningWeaponPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    public void onDiscard() {
        if (this.triggered) {
            return;
        }
        // A single discard action can notify several times before queued removal runs.
        this.triggered = true;
        flash();
        addToTop(new GainBlockAction(this.owner, this.owner, this.amount));
        addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void updateDescription() {
        this.description = STRINGS.DESCRIPTIONS[0] + this.amount + STRINGS.DESCRIPTIONS[1];
    }
}
