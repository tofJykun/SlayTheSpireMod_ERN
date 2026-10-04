package powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class SilvercatRingPower extends AbstractPower {
    public static final String POWER_ID = "SilvercatRingPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private int lastDexterity;

    public SilvercatRingPower(AbstractCreature owner, int amount) {
        ID = POWER_ID;
        name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        lastDexterity = currentDexterity();
    }

    private int currentDexterity() {
        AbstractPower dexterity = owner.getPower(DexterityPower.POWER_ID);
        return dexterity == null ? 0 : dexterity.amount;
    }

    public static void checkDexterityChange(AbstractCreature creature) {
        if (creature == null) {
            return;
        }
        AbstractPower power = creature.getPower(POWER_ID);
        if (power instanceof SilvercatRingPower) {
            ((SilvercatRingPower)power).checkDexterityChange();
        }
    }

    private void checkDexterityChange() {
        int current = currentDexterity();
        if (current == lastDexterity) {
            return;
        }
        // Update before queuing: multiple hooks can report the same change.
        lastDexterity = current;
        if (amount > 0) {
            flash();
            addToBot(new DrawCardAction(owner, amount));
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0] + amount + STRINGS.DESCRIPTIONS[1];
    }
}
