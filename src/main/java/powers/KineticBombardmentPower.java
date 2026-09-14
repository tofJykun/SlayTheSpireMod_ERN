package powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class KineticBombardmentPower extends AbstractPower {
    public static final String POWER_ID = "KineticBombardmentPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private static final int HOVER_STEP = 3;
    private static final int BONUS_PER_STEP = 10;
    private int lastBonus = -1;

    public KineticBombardmentPower(AbstractCreature owner, int amount) {
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
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateDescription();
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        int bonus = getBonusPercent();
        if (bonus != this.lastBonus) {
            updateDescription();
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return applyDamageBonus(damage, type);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        return applyDamageBonus(damage, type);
    }

    private float applyDamageBonus(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return (float)Math.floor(damage * (100.0F + getBonusPercent()) / 100.0F);
        }
        return damage;
    }

    private int getBonusPercent() {
        if (this.owner == null || !this.owner.hasPower(HoverPower.POWER_ID)) {
            return 0;
        }
        return Math.max(0, this.owner.getPower(HoverPower.POWER_ID).amount / HOVER_STEP) * BONUS_PER_STEP * this.amount;
    }

    @Override
    public void updateDescription() {
        this.lastBonus = getBonusPercent();
        this.description = DESCRIPTIONS[0] + this.lastBonus + DESCRIPTIONS[1];
    }
}
