package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BarricadeShieldPower extends AbstractPower {
    public static final String POWER_ID = "BarricadeShieldPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public BarricadeShieldPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = powerStrings.NAME;
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

    public void onPerfectGuard(AbstractMonster attacker) {
        if (attacker == null || attacker.isDeadOrEscaped() || this.amount <= 0) {
            return;
        }
        flash();
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)attacker, this.owner,
                new PoiseBreakPower((AbstractCreature)attacker, this.amount), this.amount,
                AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
