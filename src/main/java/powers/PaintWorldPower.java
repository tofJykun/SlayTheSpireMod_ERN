package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class PaintWorldPower extends AbstractPower {
    public static final String POWER_ID = "PaintWorldPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public PaintWorldPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    public void onPostPowerApply(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power == null || source != this.owner || target == null || target.isPlayer
                || !isAberration(power.ID) || power.amount <= 0) {
            return;
        }

        flash();
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster == null || monster.isDeadOrEscaped()) {
                continue;
            }
            addToBot((AbstractGameAction)new ApplyPowerAction(monster, this.owner,
                    new PoisonPower(monster, this.owner, this.amount), this.amount,
                    AbstractGameAction.AttackEffect.POISON));
            addToBot((AbstractGameAction)new ApplyPowerAction(monster, this.owner,
                    new ScarletRotPower(monster, this.owner, this.amount), this.amount,
                    AbstractGameAction.AttackEffect.POISON));
            addToBot((AbstractGameAction)new ApplyPowerAction(monster, this.owner,
                    new BloodburnPower(monster, this.owner, this.amount), this.amount,
                    AbstractGameAction.AttackEffect.POISON));
        }
    }

    private boolean isAberration(String powerId) {
        return FrostbitePower.POWER_ID.equals(powerId)
                || SleepPower.POWER_ID.equals(powerId)
                || MadnessPower.POWER_ID.equals(powerId)
                || BloodlossPower.POWER_ID.equals(powerId);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}

