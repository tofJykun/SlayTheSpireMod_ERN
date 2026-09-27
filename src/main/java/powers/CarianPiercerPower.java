package powers;

import cards.tempcards.FadingPrimalGlintstone;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class CarianPiercerPower extends AbstractPower {
    public static final String POWER_ID = "CarianPiercerPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final boolean upgradedGlintstone;

    public CarianPiercerPower(AbstractCreature owner, int amount, boolean upgradedGlintstone) {
        this.name = STRINGS.NAME + (upgradedGlintstone ? "+" : "");
        // Keep normal and upgraded grants separate when both versions are played.
        this.ID = POWER_ID + (upgradedGlintstone ? "+" : "");
        this.owner = owner;
        this.amount = amount;
        this.upgradedGlintstone = upgradedGlintstone;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info != null && info.owner instanceof AbstractMonster
                && info.type == DamageInfo.DamageType.NORMAL) {
            flash();
            FadingPrimalGlintstone card = new FadingPrimalGlintstone();
            if (this.upgradedGlintstone) {
                card.upgrade();
            }
            addToTop(new MakeTempCardInHandAction(card, this.amount));
        }
        return damageAmount;
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void updateDescription() {
        this.description = STRINGS.DESCRIPTIONS[0] + this.amount
                + STRINGS.DESCRIPTIONS[this.upgradedGlintstone ? 2 : 1];
    }
}
