package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DeterminationPower extends AbstractPower {
    public static final String POWER_ID = "DeterminationPower";
    private static final int DAMAGE_CAP = 9;
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DeterminationPower(AbstractCreature owner) {
        this.name = POWER_STRINGS.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = DAMAGE_CAP;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        this.canGoNegative = false;
        // AbstractPower sorts ascending; run after all other final damage modifiers.
        this.priority = Integer.MAX_VALUE;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return Math.min(damage, DAMAGE_CAP);
        }
        return damage;
    }

    @Override
    public void atStartOfTurn() {
        addToTop((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void updateDescription() {
        this.description = POWER_STRINGS.DESCRIPTIONS[0] + DAMAGE_CAP + POWER_STRINGS.DESCRIPTIONS[1];
    }
}
