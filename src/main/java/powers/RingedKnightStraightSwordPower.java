package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import java.util.IdentityHashMap;
import java.util.Map;

public class RingedKnightStraightSwordPower extends AbstractPower {
    public static final String POWER_ID = "RingedKnightStraightSwordPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final Map<UseCardAction, Integer> pendingEnergy = new IdentityHashMap<>();

    public RingedKnightStraightSwordPower(AbstractCreature owner, int amount) {
        this.name = STRINGS.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (amount > 0) {
            // Reserve only the energy already present before this card resolves.
            pendingEnergy.put(action, amount);
            amount = 0;
            updateDescription();
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        final Integer energy = pendingEnergy.remove(action);
        if (energy == null) {
            return;
        }
        flash();
        addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                // Another straight sword may already have added the next reward.
                if (RingedKnightStraightSwordPower.this.amount == 0 && pendingEnergy.isEmpty()
                        && owner.getPower(POWER_ID) == RingedKnightStraightSwordPower.this) {
                    addToTop(new RemoveSpecificPowerAction(owner, owner, RingedKnightStraightSwordPower.this));
                }
                addToTop(new GainEnergyAction(energy));
                isDone = true;
            }
        });
    }

    @Override
    public void updateDescription() {
        this.description = STRINGS.DESCRIPTIONS[0] + amount + STRINGS.DESCRIPTIONS[1];
    }
}
