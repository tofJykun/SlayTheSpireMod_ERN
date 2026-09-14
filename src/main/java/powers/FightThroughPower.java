package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import relics.FighterDestined;

public class FightThroughPower extends AbstractPower {
    public static final String POWER_ID = "FightThroughPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public FightThroughPower(AbstractCreature owner, int amount) {
        this.name = POWER_STRINGS.NAME;
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
        if (this.owner instanceof com.megacrit.cardcrawl.characters.AbstractPlayer
                && ((com.megacrit.cardcrawl.characters.AbstractPlayer)this.owner).hasRelic(FighterDestined.ID)) {
            addToBot((AbstractGameAction)new ApplyPowerAction(this.owner, this.owner,
                    new GreyHealthPlusPower(this.owner, this.amount), this.amount));
        } else {
            addToBot((AbstractGameAction)new ApplyPowerAction(this.owner, this.owner,
                    new GreyHealthPower(this.owner, this.amount), this.amount));
        }
    }

    @Override
    public void updateDescription() {
        boolean destined = this.owner instanceof com.megacrit.cardcrawl.characters.AbstractPlayer
                && ((com.megacrit.cardcrawl.characters.AbstractPlayer)this.owner).hasRelic(FighterDestined.ID);
        this.description = POWER_STRINGS.DESCRIPTIONS[destined ? 1 : 0] + this.amount;
    }
}
