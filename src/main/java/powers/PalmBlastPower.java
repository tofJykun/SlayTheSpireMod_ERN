package powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;

public class PalmBlastPower extends AbstractPower {
    public static final String POWER_ID = "PalmBlastPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean ended;

    public PalmBlastPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
        this.fontScale = 8.0F;
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!this.ended && this.amount > 0 && card.type == AbstractCard.CardType.ATTACK) {
            flash();
            // Snapshot the active stacks now; later Palm Blasts cannot count earlier attacks.
            addToBot(new ApplyPowerAction(this.owner, this.owner,
                    new DrawCardNextTurnPower(this.owner, this.amount), this.amount));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && !this.ended) {
            this.ended = true;
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void updateDescription() {
        this.description = STRINGS.DESCRIPTIONS[0] + this.amount + STRINGS.DESCRIPTIONS[1];
    }
}
