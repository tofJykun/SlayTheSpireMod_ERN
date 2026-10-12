package powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MushroomSoupPower extends AbstractPower {
    public static final String POWER_ID = "MushroomSoupPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static long powerIdOffset;
    private final int threshold;

    public MushroomSoupPower(AbstractCreature owner, int threshold) {
        this.ID = POWER_ID + powerIdOffset++;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.threshold = Math.max(1, threshold);
        this.amount = 0;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        this.amount++;
        if (this.amount >= this.threshold) {
            this.amount = 0;
            flash();
            addToBot(new ApplyPowerAction(this.owner, this.owner, new MushroomPower(this.owner), 1));
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(STRINGS.DESCRIPTIONS[0], this.threshold, this.amount);
    }
}
