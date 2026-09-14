package powers;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DullEmberPower extends AbstractPower {
    public static final String POWER_ID = "DullEmberPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    public DullEmberPower(AbstractCreature owner, int amount) {
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
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        if (this.amount > 0 && isStrike(card)) {
            return damage + this.amount;
        }
        return damage;
    }

    @Override
    public float modifyBlock(float blockAmount, AbstractCard card) {
        if (this.amount > 0 && isDefend(card)) {
            return blockAmount + this.amount;
        }
        return blockAmount;
    }

    public static boolean isStrike(AbstractCard card) {
        return card != null && (card.hasTag(AbstractCard.CardTags.STRIKE)
                || card.hasTag(AbstractCard.CardTags.STARTER_STRIKE)
                || (card instanceof CustomCard && ((CustomCard)card).isStrike()));
    }

    public static boolean isDefend(AbstractCard card) {
        return card != null && (card.hasTag(AbstractCard.CardTags.STARTER_DEFEND)
                || (card instanceof CustomCard && ((CustomCard)card).isDefend()));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]
                + this.amount + DESCRIPTIONS[2];
    }
}
