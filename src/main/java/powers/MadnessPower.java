package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MadnessPower extends AbstractPower {
    public static final String POWER_ID = "MadnessPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int THRESHOLD = 10;
    private static final float DAMAGE_RATIO = 0.1F;

    public MadnessPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        GnawPower.onMadnessGained(this.owner, this.amount);
        checkThreshold();
    }

    @Override
    public void stackPower(int stackAmount) {
        GnawPower.onMadnessGained(this.owner, stackAmount);
        super.stackPower(stackAmount);
        checkThreshold();
    }

    private void checkThreshold() {
        while (this.amount >= THRESHOLD && !this.owner.isDeadOrEscaped()) {
            this.amount -= THRESHOLD;
            triggerMadness();
        }

        if (this.amount <= 0) {
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            updateDescription();
        }
    }

    private void triggerMadness() {
        flash();
        int damage = Math.max(1, (int)Math.ceil(this.owner.maxHealth * DAMAGE_RATIO));
        addToBot((AbstractGameAction)new DamageAction(this.owner,
                new DamageInfo(this.owner, damage, DamageInfo.DamageType.THORNS),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        if (this.owner instanceof AbstractMonster) {
            addToBot((AbstractGameAction)new ApplyPowerAction(this.owner, this.owner,
                    new StunPower((AbstractMonster)this.owner, 1), 1));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}

