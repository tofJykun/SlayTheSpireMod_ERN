package powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ThinkingPlumPower extends AbstractPower {
    public static final String POWER_ID = "ThinkingPlumPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean released;

    public ThinkingPlumPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.DEBUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public boolean canPlayCard(AbstractCard card) {
        return released || card == null || card.isInAutoplay;
    }

    public void onPotionUsed() {
        if (released) return;
        released = true;
        flash();
        addToTop(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void stackPower(int stackAmount) { amount = -1; }

    @Override
    public void updateDescription() { description = STRINGS.DESCRIPTIONS[0]; }
}
