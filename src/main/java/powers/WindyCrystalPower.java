package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class WindyCrystalPower extends AbstractPower {
    public static final String POWER_ID = "WindyCrystalPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private static final int COUNTDOWN = 10;
    private static int powerIdOffset;
    private final int drawAmount;

    public WindyCrystalPower(AbstractCreature owner, int countdown, int drawAmount) {
        this.name = NAME;
        this.ID = POWER_ID + powerIdOffset;
        powerIdOffset++;
        this.owner = owner;
        this.amount = countdown;
        this.drawAmount = drawAmount;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.amount <= 1) {
            flash();
            addToBot((AbstractGameAction)new DrawCardAction(this.owner, this.drawAmount));
            this.amount = COUNTDOWN;
        } else {
            this.amount--;
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.drawAmount + DESCRIPTIONS[1];
    }
}
