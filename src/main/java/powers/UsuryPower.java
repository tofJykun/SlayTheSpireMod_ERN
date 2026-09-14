package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class UsuryPower extends AbstractPower {
    public static final String POWER_ID = "UsuryPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int TOKEN_DIVISOR = 4;

    public UsuryPower(AbstractCreature owner, int amount) {
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
    public void atStartOfTurn() {
        if (AbstractDungeon.player == null) {
            return;
        }
        AbstractPower currency = AbstractDungeon.player.getPower(VirtualCurrencyPower.POWER_ID);
        int tokens = currency == null ? 0 : Math.max(0, currency.amount);
        int tokenGain = tokens / TOKEN_DIVISOR * this.amount;
        if (tokenGain <= 0) {
            return;
        }
        flash();
        addToBot((AbstractGameAction)new ApplyPowerAction(this.owner, this.owner,
                new VirtualCurrencyPower(this.owner, tokenGain), tokenGain));
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0] + TOKEN_DIVISOR + DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[2] + TOKEN_DIVISOR + DESCRIPTIONS[3] + this.amount + DESCRIPTIONS[4];
        }
    }
}
