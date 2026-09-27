package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FilterPower extends AbstractPower {
    public static final String POWER_ID = "FilterPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    private int pendingDiscards;
    private boolean drawTriggered;

    public FilterPower(AbstractCreature owner, int drawAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = drawAmount;
        this.pendingDiscards = 1;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
        this.pendingDiscards += 1;
        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        if (this.drawTriggered) {
            return;
        }

        flash();
        this.drawTriggered = true;
        addToBot((AbstractGameAction)new DrawCardAction(this.owner, this.amount));
        addToBot((AbstractGameAction)new DiscardAction(this.owner, this.owner,
                this.pendingDiscards, false));
        addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]
                + this.pendingDiscards + DESCRIPTIONS[2];
    }
}
