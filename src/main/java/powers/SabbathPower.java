package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SabbathPower extends AbstractPower {
    public static final String POWER_ID = "SabbathPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int MIN_COST = 2;
    private static final int CHEMICAL_X_BONUS = 2;

    public SabbathPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!costsEnough(card)) {
            return;
        }

        flash();
        addToBot((AbstractGameAction)new GainBlockAction(this.owner, this.amount, Settings.FAST_MODE));
    }

    private boolean costsEnough(AbstractCard card) {
        if (card == null) {
            return false;
        }
        if (card.cost == -1) {
            int xValue = Math.max(0, card.energyOnUse);
            if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic("Chemical X")) {
                xValue += CHEMICAL_X_BONUS;
            }
            return xValue >= MIN_COST;
        }
        return card.costForTurn >= MIN_COST;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
