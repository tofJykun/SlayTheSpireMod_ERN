package powers;

import actions.BouquetStrengthLossAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class BouquetPower extends AbstractPower {
    public static final String POWER_ID = "BouquetPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final AbstractCreature source;

    public BouquetPower(AbstractCreature owner, AbstractCreature source, int amount) {
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
            // Resolve each hit's debuffs before the next attack, even if this hit was blocked.
            addToTop(new BouquetStrengthLossAction(owner, source, amount));
            addToTop(new ApplyPowerAction(owner, source, new VulnerablePower(owner, amount, false), amount));
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
        description = STRINGS.DESCRIPTIONS[0] + amount + STRINGS.DESCRIPTIONS[1]
                + amount + STRINGS.DESCRIPTIONS[2];
    }
}
