package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SlumberingDragoncrestPower extends AbstractPower {
    public static final String POWER_ID = "SlumberingDragoncrestPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean affectsAllEnemies;

    public SlumberingDragoncrestPower(AbstractCreature owner, int amount, boolean affectsAllEnemies) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.affectsAllEnemies = affectsAllEnemies;
        this.type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateDescription();
    }

    public void upgradeToAllEnemies() {
        this.affectsAllEnemies = true;
        updateDescription();
    }

    public void onPostPowerApply(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power == null || target != this.owner || !IntelligencePower.POWER_ID.equals(power.ID)
                || power.amount <= 0) {
            return;
        }

        flash();
        if (this.affectsAllEnemies) {
            applySleepToAllEnemies();
        } else {
            applySleepToRandomEnemy();
        }
    }

    private void applySleepToRandomEnemy() {
        AbstractMonster monster = AbstractDungeon.getMonsters().getRandomMonster(null, true,
                AbstractDungeon.cardRandomRng);
        if (monster != null) {
            addToBot((AbstractGameAction)new ApplyPowerAction(monster, this.owner,
                    new SleepPower(monster, this.amount), this.amount, true,
                    AbstractGameAction.AttackEffect.NONE));
        }
    }

    private void applySleepToAllEnemies() {
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster == null || monster.isDeadOrEscaped()) {
                continue;
            }
            addToBot((AbstractGameAction)new ApplyPowerAction(monster, this.owner,
                    new SleepPower(monster, this.amount), this.amount, true,
                    AbstractGameAction.AttackEffect.NONE));
        }
    }

    @Override
    public void updateDescription() {
        if (this.affectsAllEnemies) {
            this.description = DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }
    }
}

