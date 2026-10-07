package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class GnawPower extends AbstractPower {
    public static final String POWER_ID = "GnawPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean expiring;

    public GnawPower(AbstractCreature owner, int amount) {
        ID = POWER_ID;
        name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.DEBUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (!canTrigger()) return;
        final int bloodloss = amount;
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                if (!canTrigger()) return;
                flash();
                addToTop(new ApplyPowerAction(owner, AbstractDungeon.player,
                        new BloodlossPower(owner, bloodloss), bloodloss));
            }
        });
    }

    private boolean canTrigger() {
        return !expiring && amount > 0 && !owner.isDeadOrEscaped() && !owner.halfDead
                && owner.getPower(POWER_ID) == this;
    }

    public static void onMadnessGained(AbstractCreature target, int gained) {
        if (target == null || gained <= 0) return;
        AbstractPower power = target.getPower(POWER_ID);
        if (power instanceof GnawPower) {
            GnawPower gnaw = (GnawPower) power;
            if (!gnaw.expiring) {
                // Disable immediately, but remove through the queue to avoid mutating a power iterator.
                gnaw.expiring = true;
                gnaw.addToTop(new RemoveSpecificPowerAction(target, target, gnaw));
            }
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0] + amount + STRINGS.DESCRIPTIONS[1];
    }
}
