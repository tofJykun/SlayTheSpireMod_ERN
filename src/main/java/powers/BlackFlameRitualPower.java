package powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BlackFlameRitualPower extends AbstractPower {
    public static final String POWER_ID = "BlackFlameRitualPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public BlackFlameRitualPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    public void onEnemyLoseHp(AbstractMonster enemy, int hpLost) {
        if (hpLost <= 0 || this.amount <= 0 || enemy.currentHealth <= 0 || enemy.isDeadOrEscaped()) {
            return;
        }
        int bloodburn = (int)Math.min(Integer.MAX_VALUE, (long)hpLost * this.amount);
        flash();
        // Applying Bloodburn does not deal damage immediately; its later HP loss may trigger again.
        addToTop(new ApplyPowerAction(enemy, this.owner,
                new BloodburnPower(enemy, this.owner, bloodburn), bloodburn));
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = STRINGS.DESCRIPTIONS[0] + this.amount + STRINGS.DESCRIPTIONS[1];
    }
}
