package powers;

import actions.SyncHoverAltitudePowersAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class HoverPower extends AbstractPower {
    public static final String POWER_ID = "HoverPower";
    public static final int LOFTY_THRESHOLD = 10;
    public static final int DEEP_SPACE_THRESHOLD = 15;
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public HoverPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        this.priority = 50;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + DESCRIPTIONS[2];
    }

    @Override
    public void onInitialApplication() {
        queueAltitudeSync();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateDescription();
        queueAltitudeSync();
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        updateDescription();
        queueAltitudeSync();
    }

    @Override
    public void onRemove() {
        queueAltitudeSync();
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (shouldReduceDamage(type)) {
            return damage / 2.0F;
        }
        return damage;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null && (shouldReduceDamage(info.type)
                || (amount > 0 && info.owner == owner && info.type == DamageInfo.DamageType.THORNS))) {
            flash();
            if (this.amount == 1) {
                StormcallerPower.triggerHoverDepleted(this.owner);
            }
            addToTop((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
        }
        return damageAmount;
    }

    private boolean shouldReduceDamage(DamageInfo.DamageType type) {
        return this.amount > 0 && type != DamageInfo.DamageType.HP_LOSS && type != DamageInfo.DamageType.THORNS;
    }

    private void queueAltitudeSync() {
        addToTop((AbstractGameAction)new SyncHoverAltitudePowersAction(this.owner));
    }
}

