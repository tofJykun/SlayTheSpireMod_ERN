package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class EternalSleepPower extends AbstractPower {
    public static final String POWER_ID = "EternalSleepPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private int lastHealth;
    private boolean expiring;

    public EternalSleepPower(AbstractMonster owner) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.DEBUFF;
        this.lastHealth = owner.currentHealth;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        this.lastHealth = this.owner.currentHealth;
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
    }

    public void onHealthChanged() {
        if (this.owner.currentHealth < this.lastHealth) {
            expire();
        }
        this.lastHealth = this.owner.currentHealth;
    }

    private void expire() {
        if (!this.expiring) {
            this.expiring = true;
            addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void atStartOfTurn() {
        onHealthChanged();
        if (this.expiring || this.owner.isDeadOrEscaped() || this.owner.halfDead) {
            return;
        }
        if (this.owner.currentHealth >= this.owner.maxHealth) {
            expire();
            return;
        }
        // Poison and other start-of-turn actions can wake the enemy before this resolves.
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
                if (expiring || owner.isDeadOrEscaped() || owner.halfDead
                        || owner.getPower(POWER_ID) != EternalSleepPower.this) {
                    return;
                }
                flash();
                int heal = (int)(((long)owner.maxHealth + 19L) / 20L);
                addToTop(new ApplyPowerAction(owner, owner, new StunPower((AbstractMonster)owner, 1), 1));
                addToTop(new HealAction(owner, owner, heal));
            }
        });
    }

    @Override
    public void updateDescription() {
        this.description = STRINGS.DESCRIPTIONS[0];
    }
}
