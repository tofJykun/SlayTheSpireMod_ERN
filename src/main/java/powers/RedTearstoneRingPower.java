package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RedTearstoneRingPower extends AbstractPower {
    public static final String POWER_ID = "RedTearstoneRingPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public RedTearstoneRingPower(AbstractCreature owner, int amount) {
        this.name = POWER_STRINGS.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (this.amount > 0 && hasLethalGreyHealth()) {
            flash();
            addToBot((AbstractGameAction)new ApplyPowerAction(this.owner, this.owner,
                    new RedTearstoneRingActivePower(this.owner, this.amount), this.amount));
        }
    }

    private boolean hasLethalGreyHealth() {
        AbstractPower greyHealth = this.owner.getPower(GreyHealthPower.POWER_ID);
        if (greyHealth != null && greyHealth.amount >= this.owner.currentHealth) {
            return true;
        }
        AbstractPower greyHealthPlus = this.owner.getPower(GreyHealthPlusPower.POWER_ID);
        return greyHealthPlus != null && greyHealthPlus.amount >= this.owner.currentHealth;
    }

    @Override
    public void updateDescription() {
        this.description = POWER_STRINGS.DESCRIPTIONS[0];
    }
}
