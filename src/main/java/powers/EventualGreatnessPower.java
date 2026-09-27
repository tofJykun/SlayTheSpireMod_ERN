package powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class EventualGreatnessPower extends AbstractPower {
    public static final String POWER_ID = "EventualGreatnessPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static int powerIdOffset;
    private final int cardThreshold;
    private int strengthGainedThisTurn;
    private int dexterityGainedThisTurn;

    public EventualGreatnessPower(AbstractCreature owner, int cardThreshold) {
        this.name = POWER_STRINGS.NAME;
        this.ID = POWER_ID + powerIdOffset++;
        this.owner = owner;
        this.cardThreshold = cardThreshold;
        this.amount = 0;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        this.amount++;
        if (this.amount >= this.cardThreshold) {
            this.amount = 0;
            flash();
            addTemporaryStat(true);
            addTemporaryStat(false);
        }
        updateDescription();
    }

    private void addTemporaryStat(boolean strength) {
        if (strength) {
            strengthGainedThisTurn++;
            addToBot(new ApplyPowerAction(this.owner, this.owner,
                    new StrengthPower(this.owner, 1), 1));
        } else {
            dexterityGainedThisTurn++;
            addToBot(new ApplyPowerAction(this.owner, this.owner,
                    new DexterityPower(this.owner, 1), 1));
        }
    }

    @Override
    public void atStartOfTurn() {
        this.amount = 0;
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) {
            return;
        }
        if (strengthGainedThisTurn > 0) {
            addToBot(new ApplyPowerAction(this.owner, this.owner,
                    new StrengthPower(this.owner, -strengthGainedThisTurn), -strengthGainedThisTurn));
        }
        if (dexterityGainedThisTurn > 0) {
            addToBot(new ApplyPowerAction(this.owner, this.owner,
                    new DexterityPower(this.owner, -dexterityGainedThisTurn), -dexterityGainedThisTurn));
        }
        strengthGainedThisTurn = 0;
        dexterityGainedThisTurn = 0;
        this.amount = 0;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        String[] descriptions = POWER_STRINGS.DESCRIPTIONS;
        this.description = descriptions[0] + this.cardThreshold + descriptions[1]
                + this.amount + descriptions[2];
    }
}
