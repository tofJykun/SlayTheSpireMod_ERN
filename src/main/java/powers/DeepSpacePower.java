package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DeepSpacePower extends AbstractPower {
    public static final String POWER_ID = "DeepSpacePower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private static final int FROSTBITE = 2;
    private static final int HOVER = 1;
    private static final float DAMAGE_MULTIPLIER = 1.5F;

    public DeepSpacePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot((AbstractGameAction)new ApplyPowerAction(this.owner, this.owner,
                new FrostbitePower(this.owner, FROSTBITE), FROSTBITE));
        addToBot((AbstractGameAction)new ApplyPowerAction(this.owner, this.owner,
                new HoverPower(this.owner, HOVER), HOVER));
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        // Apply the multiplier after additive bonuses such as Strength and Vigor.
        if (type == DamageInfo.DamageType.NORMAL) {
            return (float)Math.ceil(damage * DAMAGE_MULTIPLIER);
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
