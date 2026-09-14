package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class StormcallerPower extends AbstractPower {
    public static final String POWER_ID = "StormcallerPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public StormcallerPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    public static void triggerHoverDepleted(AbstractCreature owner) {
        if (owner == null || !owner.hasPower(POWER_ID)) {
            return;
        }

        AbstractPower power = owner.getPower(POWER_ID);
        if (power == null || power.amount <= 0) {
            return;
        }

        power.flash();
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ReducePowerAction(
                owner, owner, POWER_ID, 1));
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction(
                owner, owner, new StormcallerNoPlayPower(owner, 1), 1));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
