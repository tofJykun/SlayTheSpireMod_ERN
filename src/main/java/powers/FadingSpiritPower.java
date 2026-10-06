package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FadingSpiritPower extends AbstractPower {
    public static final String POWER_ID = "FadingSpiritPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public FadingSpiritPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) return;
        final int loss = amount;
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (isDone) return;
                // Reverse the grant even when temporary loss removed the zero-stack power.
                AbstractPower spirit = owner.getPower(SpiritPower.POWER_ID);
                if (spirit != null) {
                    spirit.stackPower(-loss);
                } else if (loss != 0) {
                    owner.powers.add(new SpiritPower(owner, -loss));
                }
                AbstractDungeon.onModifyPower();
                isDone = true;
            }
        });
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }
}
