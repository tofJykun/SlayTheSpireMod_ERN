package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CrystalCardHelper;

public class QuasicrystalPower extends AbstractPower {
    public static final String POWER_ID = "QuasicrystalPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private static final int CARDS_PER_TRIGGER = 2;
    private int crystalCardsPlayed;

    public QuasicrystalPower(AbstractCreature owner, int amount) {
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
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!CrystalCardHelper.isCrystalCard(card)) {
            return;
        }
        this.crystalCardsPlayed++;
        if (this.crystalCardsPlayed < CARDS_PER_TRIGGER) {
            return;
        }
        this.crystalCardsPlayed -= CARDS_PER_TRIGGER;
        flash();
        for (int i = 0; i < this.amount; i++) {
            AbstractCard crystal = CrystalCardHelper.randomCrystalCard();
            if (crystal != null) {
                addToBot((AbstractGameAction)new MakeTempCardInHandAction(crystal, 1));
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
