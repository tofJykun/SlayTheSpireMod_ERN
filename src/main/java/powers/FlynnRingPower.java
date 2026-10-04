package powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class FlynnRingPower extends AbstractPower {
    public static final String POWER_ID = "FlynnRingPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public FlynnRingPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        this.isTurnBased = false;
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
        if (type != DamageInfo.DamageType.NORMAL || this.owner == null) return damage;
        AbstractPower dexterity = this.owner.getPower(DexterityPower.POWER_ID);
        if (dexterity == null || dexterity.amount >= 0) return damage;
        // Keep the fractional result until the engine finishes all damage multipliers.
        return damage * (1.0F - dexterity.amount * (this.amount / 100.0F));
    }

    @Override
    public void updateDescription() {
        this.description = String.format(STRINGS.DESCRIPTIONS[0], this.amount);
    }
}
