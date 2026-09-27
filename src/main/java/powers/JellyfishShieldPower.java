package powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class JellyfishShieldPower extends AbstractPower {
    public static final String POWER_ID = "JellyfishShieldPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public JellyfishShieldPower(AbstractCreature owner, int bonusPercent) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = bonusPercent;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
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
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        // Apply the summed bonus after additive modifiers such as Strength and Vigor.
        return type == DamageInfo.DamageType.NORMAL ? damage * (1.0F + this.amount / 100.0F) : damage;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void updateDescription() {
        this.description = STRINGS.DESCRIPTIONS[0] + this.amount + STRINGS.DESCRIPTIONS[1];
    }
}
