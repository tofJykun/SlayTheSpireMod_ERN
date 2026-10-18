package powers;

import actions.RandomCardsAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class EmergePower extends AbstractPower {
    public static final String POWER_ID = "EmergePower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public EmergePower(AbstractCreature owner, int amount) {
        ID = POWER_ID;
        name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (amount <= 0) return;
        flash();
        addToBot(new RandomCardsAction(amount));
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0] + amount + STRINGS.DESCRIPTIONS[1];
    }
}
