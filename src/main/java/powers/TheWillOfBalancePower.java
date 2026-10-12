package powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class TheWillOfBalancePower extends AbstractPower {
    public static final String POWER_ID = "TheWillOfBalancePower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static int nextId;
    private boolean triggered;

    public TheWillOfBalancePower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID + nextId++;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onInitialApplication() { checkThreshold(); }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) { checkThreshold(); }

    private void checkThreshold() {
        // Normality's full-turn count includes cards played before this power was gained.
        if (this.triggered || AbstractDungeon.actionManager == null
                || AbstractDungeon.actionManager.cardsPlayedThisTurn.size() < 3) return;
        this.triggered = true;
        flash();
        addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount), amount));
        addToBot(new ApplyPowerAction(owner, owner, new LoseStrengthPower(owner, amount), amount));
        addToBot(new ApplyPowerAction(owner, owner, new DexterityPower(owner, amount), amount));
        addToBot(new ApplyPowerAction(owner, owner, new LoseDexterityPower(owner, amount), amount));
    }

    @Override
    public void atStartOfTurn() { this.triggered = false; }

    @Override
    public void updateDescription() {
        this.description = STRINGS.DESCRIPTIONS[0] + this.amount + STRINGS.DESCRIPTIONS[1]
                + this.amount + STRINGS.DESCRIPTIONS[2];
    }
}
