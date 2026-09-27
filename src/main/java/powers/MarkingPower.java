package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import patches.MarkingDamagePatch;

public class MarkingPower extends AbstractPower {
    public static final String POWER_ID = "MarkingPower";
    public static final int DAMAGE_THRESHOLD = 15;
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final AbstractCreature source;

    public MarkingPower(AbstractCreature owner, AbstractCreature source, int amount) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.source = source;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }

    public void onPlayerEndTurn() {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
                if (owner.getPower(POWER_ID) != MarkingPower.this) {
                    return;
                }
                addToTop(new RemoveSpecificPowerAction(owner, owner, MarkingPower.this));
                if (!owner.isDeadOrEscaped()
                        && MarkingDamagePatch.getDamageThisTurn(owner) >= DAMAGE_THRESHOLD) {
                    flash();
                    addToTop(new LoseHPAction(owner, MarkingPower.this.source, MarkingPower.this.amount));
                }
            }
        });
    }

    @Override
    public void updateDescription() {
        this.description = STRINGS.DESCRIPTIONS[0] + DAMAGE_THRESHOLD + STRINGS.DESCRIPTIONS[1]
                + this.amount + STRINGS.DESCRIPTIONS[2] + MarkingDamagePatch.getDamageThisTurn(this.owner)
                + STRINGS.DESCRIPTIONS[3];
    }
}
