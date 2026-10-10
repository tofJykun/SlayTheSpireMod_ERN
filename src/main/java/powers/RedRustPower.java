package powers;

import actions.RedRustDuplicateAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RedRustPower extends AbstractPower {
    public static final String POWER_ID = "RedRustPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public RedRustPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    public void duplicateApplication(AbstractPower applied, AbstractCreature source, int stacks) {
        AbstractPower copy = InfectionStrikePower.makeDuplicatePower(applied, owner, source, stacks);
        if (copy == null) return;
        flash();
        --amount;
        updateDescription();
        // Do not mutate the power list while ApplyPowerAction is iterating it.
        if (amount == 0) {
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    isDone = true;
                    // Applying Red Rust itself can replenish this instance before cleanup.
                    if (owner.getPower(POWER_ID) == RedRustPower.this && RedRustPower.this.amount <= 0) {
                        addToTop(new RemoveSpecificPowerAction(owner, owner, RedRustPower.this));
                    }
                }
            });
        }
        addToBot(new RedRustDuplicateAction(owner, source, copy, stacks));
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
