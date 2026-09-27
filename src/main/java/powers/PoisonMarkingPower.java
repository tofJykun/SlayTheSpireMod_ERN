package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import patches.MarkingDamagePatch;

public class PoisonMarkingPower extends AbstractPower {
    public static final String POWER_ID = "PoisonMarkingPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final AbstractCreature source;

    public PoisonMarkingPower(AbstractCreature owner, AbstractCreature source, int amount) {
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
                if (owner.getPower(POWER_ID) != PoisonMarkingPower.this) {
                    return;
                }
                addToTop(new RemoveSpecificPowerAction(owner, owner, PoisonMarkingPower.this));
                if (!owner.isDeadOrEscaped()
                        && MarkingDamagePatch.getDamageThisTurn(owner) >= MarkingPower.DAMAGE_THRESHOLD) {
                    flash();
                    addToTop(new ApplyPowerAction(owner, PoisonMarkingPower.this.source,
                            new PoisonPower(owner, PoisonMarkingPower.this.source, PoisonMarkingPower.this.amount),
                            PoisonMarkingPower.this.amount));
                }
            }
        });
    }

    @Override
    public void updateDescription() {
        this.description = STRINGS.DESCRIPTIONS[0] + MarkingPower.DAMAGE_THRESHOLD + STRINGS.DESCRIPTIONS[1]
                + this.amount + STRINGS.DESCRIPTIONS[2] + MarkingDamagePatch.getDamageThisTurn(this.owner)
                + STRINGS.DESCRIPTIONS[3];
    }
}
