package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import java.util.ArrayList;

public class BookOfRevelationPower extends AbstractPower {
    public static final String POWER_ID = "BookOfRevelationPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int DEATH_DAMAGE = 99999;
    private static final int TURNS_TO_DEATH = 7;
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private final ArrayList<Integer> counters = new ArrayList<Integer>();

    public BookOfRevelationPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        this.counters.add(TURNS_TO_DEATH);
        this.amount = TURNS_TO_DEATH;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        int timersToAdd = Math.max(1, stackAmount);
        for (int i = 0; i < timersToAdd; i++) {
            this.counters.add(TURNS_TO_DEATH);
        }
        refreshAmount();
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        boolean shouldDie = false;
        for (int i = this.counters.size() - 1; i >= 0; i--) {
            int counter = this.counters.get(i) - 1;
            if (counter <= 0) {
                shouldDie = true;
                this.counters.remove(i);
            } else {
                this.counters.set(i, counter);
            }
        }

        if (shouldDie) {
            flash();
            addToBot((AbstractGameAction)new VFXAction((AbstractGameEffect)new LightningEffect(this.owner.hb.cX, this.owner.hb.cY)));
            addToBot((AbstractGameAction)new LoseHPAction(this.owner, this.owner, DEATH_DAMAGE));
        }

        if (this.counters.isEmpty()) {
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        } else {
            refreshAmount();
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    private void refreshAmount() {
        int min = TURNS_TO_DEATH;
        for (Integer counter : this.counters) {
            if (counter != null && counter < min) {
                min = counter;
            }
        }
        this.amount = min;
    }
}
