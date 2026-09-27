package powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DeadlyDancePower extends AbstractPower {
    public static final String POWER_ID = "DeadlyDancePower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DeadlyDancePower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
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

    public void onDebuffApplied(AbstractPower power, AbstractCreature target, AbstractCreature source,
                                int appliedAmount) {
        if (power == null || source != this.owner || !(target instanceof AbstractMonster)
                || target.isDeadOrEscaped() || power.type != PowerType.DEBUFF || appliedAmount == 0
                || (appliedAmount < 0 && !power.canGoNegative && power.amount != -1)) {
            return;
        }
        flash();
        addToBot(new GainBlockAction(this.owner, this.amount, Settings.FAST_MODE));
    }

    @Override
    public void updateDescription() {
        this.description = STRINGS.DESCRIPTIONS[0] + this.amount + STRINGS.DESCRIPTIONS[1];
    }
}
