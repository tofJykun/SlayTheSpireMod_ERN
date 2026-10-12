package powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class WingOfAstelPower extends AbstractPower {
    public static final String POWER_ID = "WingOfAstelPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean triggered;

    public WingOfAstelPower(AbstractCreature owner, int amount) {
        ID = POWER_ID;
        name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (triggered) return;
        triggered = true;
        flash();
        // Clear the spent effect before debuff callbacks can grant a new one.
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped() && !monster.halfDead) {
                addToBot(new ApplyPowerAction(monster, owner,
                        new PoiseBreakPower(monster, amount), amount));
            }
        }
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0] + amount + STRINGS.DESCRIPTIONS[1];
    }
}
