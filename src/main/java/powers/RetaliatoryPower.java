package powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class RetaliatoryPower extends AbstractPower {
    public static final String POWER_ID = "RetaliatoryPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private int usesThisTurn;

    public RetaliatoryPower(AbstractCreature owner, int amount) {
        ID = POWER_ID;
        name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    public int remainingUses() {
        return Math.max(0, amount - usesThisTurn);
    }

    public void onVigorConsumed(int vigor) {
        if (vigor <= 0 || remainingUses() == 0) return;
        usesThisTurn++;
        flash();
        updateDescription();
        addToTop(new ApplyPowerAction(owner, owner, new VigorPower(owner, vigor), vigor));
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        usesThisTurn = 0;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0] + amount + STRINGS.DESCRIPTIONS[1]
                + remainingUses() + STRINGS.DESCRIPTIONS[2];
    }
}
