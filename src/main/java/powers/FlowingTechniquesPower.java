package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;

public class FlowingTechniquesPower extends AbstractPower {
    public static final String POWER_ID = "FlowingTechniquesPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int CARD_THRESHOLD = 6;

    public FlowingTechniquesPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer || AbstractDungeon.actionManager == null
                || AbstractDungeon.actionManager.cardsPlayedThisTurn.size() < CARD_THRESHOLD) {
            return;
        }

        flash();
        addToBot((AbstractGameAction)new ApplyPowerAction(this.owner, this.owner,
                new DrawCardNextTurnPower(this.owner, this.amount), this.amount));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + CARD_THRESHOLD + DESCRIPTIONS[1]
                + this.amount + DESCRIPTIONS[2];
    }
}
