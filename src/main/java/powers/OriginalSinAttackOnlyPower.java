package powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class OriginalSinAttackOnlyPower extends AbstractPower {
    public static final String POWER_ID = "OriginalSinAttackOnlyPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public OriginalSinAttackOnlyPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override public boolean canPlayCard(AbstractCard card) {
        return card == null || card.type == AbstractCard.CardType.ATTACK;
    }

    @Override public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override public void stackPower(int amount) { this.amount = -1; }
    @Override public void updateDescription() { this.description = STRINGS.DESCRIPTIONS[0]; }
}
