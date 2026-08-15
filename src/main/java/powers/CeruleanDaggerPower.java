package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;

public class CeruleanDaggerPower extends AbstractPower {
    public static final String POWER_ID = "CeruleanDaggerPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int ENERGY_NEXT_TURN = 2;
    private static final int DRAW_NEXT_TURN = 2;
    private boolean drawNextTurn;

    public CeruleanDaggerPower(AbstractCreature owner, boolean drawNextTurn) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.drawNextTurn = drawNextTurn;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        if (stackAmount > 1) {
            this.drawNextTurn = true;
        }
        this.amount = -1;
        updateDescription();
    }

    public void onSuccessfulParry() {
        flash();
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction(
                this.owner, this.owner, new EnergizedPower(this.owner, ENERGY_NEXT_TURN), ENERGY_NEXT_TURN));
        if (this.drawNextTurn) {
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction(
                    this.owner, this.owner, new DrawCardNextTurnPower(this.owner, DRAW_NEXT_TURN), DRAW_NEXT_TURN));
        }
    }

    @Override
    public void updateDescription() {
        this.description = this.drawNextTurn ? DESCRIPTIONS[1] : DESCRIPTIONS[0];
    }
}
