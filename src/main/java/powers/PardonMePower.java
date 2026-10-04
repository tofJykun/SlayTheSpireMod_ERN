package powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class PardonMePower extends AbstractPower {
    public static final String POWER_ID = "PardonMePower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private int dexterityToReturn;

    public PardonMePower(AbstractCreature owner, int amount) {
        ID = POWER_ID;
        name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (amount <= 0) return;
        flash();
        final int loss = amount;
        addToBot(new ApplyPowerAction(owner, owner, new DexterityPower(owner, -loss), -loss) {
            private boolean recorded;

            @Override
            public void update() {
                if (recorded) {
                    super.update();
                    return;
                }
                int before = currentDexterity();
                super.update();
                // Artifact and the stat floor can prevent some or all of the loss.
                dexterityToReturn += Math.min(loss, Math.max(0, before - currentDexterity()));
                recorded = true;
                updateDescription();
            }
        });
    }

    private int currentDexterity() {
        AbstractPower dexterity = owner.getPower(DexterityPower.POWER_ID);
        return dexterity == null ? 0 : dexterity.amount;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) return;
        if (dexterityToReturn > 0) {
            addToBot(new ApplyPowerAction(owner, owner,
                    new DexterityPower(owner, dexterityToReturn), dexterityToReturn));
        }
        dexterityToReturn = 0;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0] + amount + STRINGS.DESCRIPTIONS[1]
                + dexterityToReturn + STRINGS.DESCRIPTIONS[2];
    }
}
