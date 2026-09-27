package powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DancingBladePower extends AbstractPower {
    public static final String POWER_ID = "DancingBladePower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private boolean consumed;

    public DancingBladePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.DEBUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public boolean canPlayCard(AbstractCard card) {
        return this.consumed || card == null || card.isInAutoplay || card.type == AbstractCard.CardType.SKILL;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card != null && !this.consumed) {
            this.consumed = true;
            // Remove the old restriction before another Dancing Blade applies a new one.
            addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount = -1;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
