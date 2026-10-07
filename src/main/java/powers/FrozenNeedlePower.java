package powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.FrozenNeedleAttackContext;

public class FrozenNeedlePower extends AbstractPower {
    public static final String POWER_ID = "FrozenNeedlePower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public FrozenNeedlePower(AbstractCreature owner) {
        ID = POWER_ID;
        name = STRINGS.NAME;
        this.owner = owner;
        amount = -1;
        type = PowerType.DEBUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        int refund = FrozenNeedleAttackContext.refund(info);
        if (refund > 0) {
            flash();
            addToTop(new GainEnergyAction(refund));
        }
        return damageAmount;
    }

    @Override
    public void stackPower(int stackAmount) { }

    @Override
    public void updateDescription() { description = STRINGS.DESCRIPTIONS[0]; }
}
