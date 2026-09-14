package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.BulkPotionQueue;

import java.util.ArrayList;

public class WantToDrinkThisPower extends AbstractPower {
    public static final String POWER_ID = "WantToDrinkThisPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int TURNS_PER_POTION = 2;
    private final ArrayList<Integer> counters = new ArrayList<Integer>();

    public WantToDrinkThisPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        for (int i = 0; i < amount; i++) {
            this.counters.add(1);
        }
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        for (int i = 0; i < stackAmount; i++) {
            this.counters.add(1);
        }
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        int potionsToGain = 0;
        for (int i = 0; i < this.counters.size(); i++) {
            int counter = this.counters.get(i) + 1;
            if (counter >= TURNS_PER_POTION) {
                potionsToGain++;
                counter = 0;
            }
            this.counters.set(i, counter);
        }

        if (potionsToGain <= 0) {
            updateDescription();
            return;
        }

        flash();
        for (int i = 0; i < potionsToGain; i++) {
            addToBot((AbstractGameAction)new ObtainPotionAction(BulkPotionQueue.getRandomBulkPotion()));
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }
}
