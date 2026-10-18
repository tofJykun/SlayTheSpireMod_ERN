package powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CombatState;

public class StickyPotionPower extends AbstractPower {
    public static final String POWER_ID = "StickyPotionPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public StickyPotionPower(AbstractCreature owner, int amount) {
        ID = POWER_ID;
        name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        amount += stackAmount;
        fontScale = 8.0F;
        updateDescription();
    }

    public void onPotionUsed() {
        if (amount <= 0 || !CombatState.isInCombat()) return;
        flash();
        addToBot(new GainBlockAction(owner, owner, amount));
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0] + amount + STRINGS.DESCRIPTIONS[1];
    }
}
