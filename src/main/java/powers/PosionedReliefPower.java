package powers;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CombatState;

public class PosionedReliefPower extends AbstractPower {
    public static final String POWER_ID = "PosionedReliefPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static long nextId;
    private final AbstractMonster recipient;
    private boolean triggered;

    public PosionedReliefPower(AbstractCreature owner, AbstractMonster recipient, int amount) {
        this.ID = POWER_ID + nextId++;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.recipient = recipient;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    private boolean validTarget() {
        return CombatState.isInCombat() && recipient != null && !recipient.isDeadOrEscaped()
                && !recipient.halfDead && recipient.currentHealth > 0;
    }

    @Override
    public void atStartOfTurn() {
        if (triggered) {
            return;
        }
        triggered = true;
        flash();
        addToBot(new RemoveSpecificPowerAction(owner, owner, this.ID));
        if (validTarget()) {
            addToBot(new LoseHPAction(recipient, owner, this.amount) {
                @Override
                public void update() {
                    // Earlier start-of-turn effects may kill this same target before this action runs.
                    if (!validTarget()) {
                        this.isDone = true;
                        return;
                    }
                    super.update();
                }
            });
        }
    }

    @Override
    public void updateDescription() {
        this.description = String.format(STRINGS.DESCRIPTIONS[0], recipient.name, this.amount);
    }
}
