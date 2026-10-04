package powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SwordOfNightPower extends AbstractPower {
    public static final String POWER_ID = "SwordOfNightPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final AbstractCreature source;

    public SwordOfNightPower(AbstractCreature owner, AbstractCreature source, int amount) {
        ID = POWER_ID;
        name = STRINGS.NAME;
        this.owner = owner;
        this.source = source;
        this.amount = amount;
        type = PowerType.DEBUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (amount > 0 && info != null && info.owner != null && info.owner != owner
                && info.type == DamageInfo.DamageType.NORMAL) {
            flash();
            // Fully blocked hits still trigger; resolve Sleep before the next hit.
            addToTop(new ApplyPowerAction(owner, source, new SleepPower(owner, amount), amount));
        }
        return damageAmount;
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0] + amount + STRINGS.DESCRIPTIONS[1];
    }
}
