package powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CombatState;

public class WavesOfDarknessPower extends AbstractPower {
    public static final String POWER_ID = "WavesOfDarknessPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static long powerIdOffset;
    private final int threshold;
    private final int drawAmount;

    public WavesOfDarknessPower(AbstractCreature owner, int threshold, int drawAmount) {
        this.ID = POWER_ID + powerIdOffset++;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.threshold = threshold;
        this.drawAmount = drawAmount;
        this.amount = 0;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    public void onDebuffApplied(AbstractPower power, AbstractCreature target, AbstractCreature source,
                                int appliedAmount) {
        if (!CombatState.isInCombat() || power == null || source != this.owner
                || !(target instanceof AbstractMonster) || target.isDeadOrEscaped()
                || power.type != PowerType.DEBUFF || appliedAmount == 0
                || (appliedAmount < 0 && !power.canGoNegative && power.amount != -1)) {
            return;
        }
        this.amount++;
        if (this.amount >= this.threshold) {
            this.amount -= this.threshold;
            flash();
            addToBot(new DrawCardAction(this.owner, this.drawAmount));
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = STRINGS.DESCRIPTIONS[0] + this.threshold + STRINGS.DESCRIPTIONS[1]
                + this.drawAmount + STRINGS.DESCRIPTIONS[2] + this.amount + STRINGS.DESCRIPTIONS[3];
    }
}
