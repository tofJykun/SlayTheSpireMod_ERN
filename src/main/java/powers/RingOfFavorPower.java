package powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RingOfFavorPower extends AbstractPower {
    public static final String POWER_ID = "RingOfFavorPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public RingOfFavorPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        this.canGoNegative = true;
        this.isTurnBased = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        changeDraw(this.amount);
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        changeDraw(stackAmount);
        updateDescription();
        if (this.amount == 0) {
            addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    @Override
    public void reducePower(int reduceAmount) {
        stackPower(-reduceAmount);
    }

    @Override
    public void onRemove() {
        changeDraw(-this.amount);
    }

    private void changeDraw(int amount) {
        if (this.owner instanceof AbstractPlayer) {
            ((AbstractPlayer)this.owner).gameHandSize += amount;
        }
    }

    @Override
    public void updateDescription() {
        this.type = this.amount < 0 ? PowerType.DEBUFF : PowerType.BUFF;
        this.description = String.format(STRINGS.DESCRIPTIONS[this.amount < 0 ? 1 : 0], Math.abs(this.amount));
    }
}
