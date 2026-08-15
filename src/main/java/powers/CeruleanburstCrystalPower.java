package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class CeruleanburstCrystalPower extends AbstractPower {
    public static final String POWER_ID = "CeruleanburstCrystalPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private static final int COUNTDOWN = 10;
    private static int powerIdOffset;

    public CeruleanburstCrystalPower(AbstractCreature owner, int countdown) {
        this.name = NAME;
        this.ID = POWER_ID + powerIdOffset;
        powerIdOffset++;
        this.owner = owner;
        this.amount = countdown;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.amount <= 1) {
            flash();
            addToBot((AbstractGameAction)new GainEnergyAction(1));
            this.amount = COUNTDOWN;
        } else {
            this.amount--;
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
