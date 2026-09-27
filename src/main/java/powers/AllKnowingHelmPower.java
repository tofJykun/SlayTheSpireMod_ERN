package powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class AllKnowingHelmPower extends AbstractPower {
    public static final String POWER_ID = "AllKnowingHelmPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public AllKnowingHelmPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    public static boolean shouldMakeFree(AbstractCard card) {
        if (card == null || card.cost < -1 || AbstractDungeon.player == null) {
            return false;
        }
        AbstractPower power = AbstractDungeon.player.getPower(POWER_ID);
        return power != null && power.amount > 0;
    }

    public static boolean shouldRandomize(AbstractCard card) {
        return shouldMakeFree(card) && (card.target == AbstractCard.CardTarget.ENEMY
                || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card == null || this.amount <= 0) {
            return;
        }
        flash();
        this.amount--;
        updateDescription();
        if (this.amount == 0) {
            // Remove before the played card's actions can apply a fresh set of charges.
            addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
        this.fontScale = 8.0F;
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void updateDescription() {
        this.description = STRINGS.DESCRIPTIONS[0] + this.amount + STRINGS.DESCRIPTIONS[1];
    }
}
