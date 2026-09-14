package powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class DeterrencePower extends AbstractPower {
    public static final String POWER_ID = "DeterrencePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public DeterrencePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateDescription();
    }

    public static void modifyDamage(DamageInfo info, AbstractCreature owner, AbstractCreature target) {
        if (info == null || owner == null || target == null || owner == target
                || info.type != DamageInfo.DamageType.NORMAL || info.output <= 0
                || !owner.hasPower(VulnerablePower.POWER_ID) || !target.hasPower(POWER_ID)) {
            return;
        }

        AbstractPower power = target.getPower(POWER_ID);
        if (power == null || power.amount <= 0) {
            return;
        }

        int oldOutput = info.output;
        float multiplier = Math.max(0.0F, (100.0F - power.amount) / 100.0F);
        info.output = (int)Math.floor(info.output * multiplier);
        if (info.output != oldOutput || info.output != info.base) {
            info.isModified = true;
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
