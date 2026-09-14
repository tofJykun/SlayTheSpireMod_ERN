package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import relics.FighterDestined;

public class BlessedDewPower extends AbstractPower {
    public static final String POWER_ID = "BlessedDewPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    public BlessedDewPower(AbstractCreature owner, int amount) {
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
    public void atStartOfTurn() {
        flash();
        String greyHealthId = hasFighterDestined()
                ? GreyHealthPlusPower.POWER_ID : GreyHealthPower.POWER_ID;
        addToBot((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, greyHealthId, this.amount));
    }

    @Override
    public void updateDescription() {
        if (hasFighterDestined()) {
            this.description = DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }
    }

    private boolean hasFighterDestined() {
        return this.owner instanceof AbstractPlayer && ((AbstractPlayer)this.owner).hasRelic(FighterDestined.ID);
    }
}
