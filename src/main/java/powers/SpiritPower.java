package powers;

import cards.tempcards.AbstractPhantomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SpiritPower extends AbstractPower {
    public static final String POWER_ID = "SpiritPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SpiritPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        updateDescription();
        PowerIconHelper.load(this, POWER_ID);
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount == 0) {
            addToTop((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    public static void applyPhantomNumber(AbstractCard card) {
        if (AbstractDungeon.player == null || card == null || card.baseMagicNumber < 0 || !isPhantomCard(card)) {
            return;
        }

        AbstractPower power = AbstractDungeon.player.getPower(POWER_ID);
        int spirit = power == null ? 0 : power.amount;
        int newMagicNumber = card.baseMagicNumber + spirit;
        if (newMagicNumber < 0) {
            newMagicNumber = 0;
        }
        card.magicNumber = newMagicNumber;
        card.isMagicNumberModified = card.magicNumber != card.baseMagicNumber;
    }

    private static boolean isPhantomCard(AbstractCard card) {
        return card instanceof AbstractPhantomCard;
    }
}

