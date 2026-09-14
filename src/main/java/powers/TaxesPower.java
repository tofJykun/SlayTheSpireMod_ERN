package powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class TaxesPower extends AbstractPower {
    public static final String POWER_ID = "TaxesPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    private int remainingThisTurn;

    public TaxesPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.remainingThisTurn = amount;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        this.remainingThisTurn = this.amount;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        this.remainingThisTurn += stackAmount;
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (canMakeFree(card)) {
            flash();
            this.remainingThisTurn--;
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]
                + this.remainingThisTurn + DESCRIPTIONS[2];
    }

    public boolean canMakeFree(AbstractCard card) {
        return this.remainingThisTurn > 0
                && card != null
                && card.cost >= 0
                && AbstractDungeon.player != null
                && AbstractDungeon.player.hand != null
                && AbstractDungeon.player.hand.contains(card);
    }

    public static boolean shouldMakeFree(AbstractCard card) {
        if (AbstractDungeon.player == null || !AbstractDungeon.player.hasPower(POWER_ID)) {
            return false;
        }
        AbstractPower power = AbstractDungeon.player.getPower(POWER_ID);
        return power instanceof TaxesPower && ((TaxesPower)power).canMakeFree(card);
    }
}
