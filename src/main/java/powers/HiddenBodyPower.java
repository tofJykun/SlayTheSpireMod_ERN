package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import general.CombatState;

public class HiddenBodyPower extends AbstractPower {
    public static final String POWER_ID = "HiddenBodyPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public HiddenBodyPower(AbstractCreature owner, int amount) {
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
    public void atStartOfTurn() {
        flash();
        this.amount = Math.max(0, this.amount - countAttackingEnemies());
        updateDescription();
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)monster, this.owner,
                        new WeakPower((AbstractCreature)monster, this.amount, false), this.amount, true,
                        AbstractGameAction.AttackEffect.NONE));
            }
        }
        addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    public static int countAttackingEnemies() {
        if (!CombatState.isInCombat() || AbstractDungeon.getMonsters() == null) {
            return 0;
        }
        int count = 0;
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster != null && !monster.isDeadOrEscaped() && !monster.halfDead && isAttackIntent(monster.intent)) {
                count++;
            }
        }
        return count;
    }

    private static boolean isAttackIntent(AbstractMonster.Intent intent) {
        return intent == AbstractMonster.Intent.ATTACK
                || intent == AbstractMonster.Intent.ATTACK_BUFF
                || intent == AbstractMonster.Intent.ATTACK_DEBUFF
                || intent == AbstractMonster.Intent.ATTACK_DEFEND;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}

