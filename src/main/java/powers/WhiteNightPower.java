package powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.WhiteNightProgress;

public class WhiteNightPower extends AbstractPower {
    public static final String POWER_ID = "WhiteNightPower";
    private boolean resolvedVictory;

    public WhiteNightPower(AbstractCreature owner) {
        ID = POWER_ID;
        name = CardCrawlGame.languagePack.getPowerStrings(ID).NAME;
        this.owner = owner;
        amount = -1;
        type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) { amount = -1; }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type != AbstractCard.CardType.POWER) action.exhaustCard = true;
    }

    @Override
    public void onVictory() {
        if (!resolvedVictory) {
            resolvedVictory = true;
            WhiteNightProgress.finishVictory();
        }
    }

    @Override
    public void updateDescription() {
        description = CardCrawlGame.languagePack.getPowerStrings(POWER_ID).DESCRIPTIONS[0];
    }
}
