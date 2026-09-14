package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BladeOfCallingPower extends AbstractPower {
    public static final String POWER_ID = "BladeOfCallingPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final AbstractCreature source;

    public BladeOfCallingPower(AbstractCreature owner, AbstractCreature source, int amount) {
        this.name = POWER_STRINGS.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.source = source;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0
                && info != null
                && info.owner != null
                && info.owner != this.owner
                && info.type != DamageInfo.DamageType.HP_LOSS
                && info.type != DamageInfo.DamageType.THORNS) {
            flash();
            addToTop((AbstractGameAction)new ApplyPowerAction(this.owner, this.source,
                    new BloodburnPower(this.owner, this.source, this.amount), this.amount,
                    true, AbstractGameAction.AttackEffect.FIRE));
        }
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        this.description = POWER_STRINGS.DESCRIPTIONS[0] + this.amount + POWER_STRINGS.DESCRIPTIONS[1];
    }
}
