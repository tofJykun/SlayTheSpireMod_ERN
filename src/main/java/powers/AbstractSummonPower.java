package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

public abstract class AbstractSummonPower extends AbstractPower {
    protected final String[] descriptions;
    private final int blockAmount;
    private final AbstractCard phantomCard;
    private final String summonImagePath;

    protected AbstractSummonPower(AbstractCreature owner, String powerId, String name, String[] descriptions,
                                  int blockAmount, AbstractCard phantomCard, String summonImagePath) {
        this.ID = powerId;
        this.name = name;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        this.descriptions = descriptions;
        this.blockAmount = blockAmount;
        this.phantomCard = phantomCard;
        this.summonImagePath = summonImagePath;
        loadRegion("blur");
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        removeOtherSummons();
        triggerSummonEffect();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        removeOtherSummons();
        triggerSummonEffect();
    }

    public String getSummonImagePath() {
        return this.summonImagePath;
    }

    public static AbstractSummonPower getActiveSummon(AbstractCreature creature) {
        if (creature == null) {
            return null;
        }
        AbstractSummonPower active = null;
        for (AbstractPower power : creature.powers) {
            if (power instanceof AbstractSummonPower) {
                active = (AbstractSummonPower)power;
            }
        }
        return active;
    }

    public static void removeIfBlockGone(AbstractCreature creature) {
        if (creature == null || !creature.isPlayer || creature.currentBlock > 0) {
            return;
        }
        AbstractSummonPower active = getActiveSummon(creature);
        if (active != null) {
            AbstractDungeon.actionManager.addToTop((AbstractGameAction)new RemoveSpecificPowerAction(
                    creature, creature, active));
        }
    }

    private void removeOtherSummons() {
        ArrayList<AbstractPower> toRemove = new ArrayList<>();
        for (AbstractPower power : this.owner.powers) {
            if (power instanceof AbstractSummonPower && power != this) {
                toRemove.add(power);
            }
        }
        for (AbstractPower power : toRemove) {
            AbstractDungeon.actionManager.addToTop((AbstractGameAction)new RemoveSpecificPowerAction(
                    this.owner, this.owner, power));
        }
    }

    private void triggerSummonEffect() {
        addToBot((AbstractGameAction)new GainBlockAction(this.owner, this.owner, this.blockAmount));
        addToBot((AbstractGameAction)new MakeTempCardInHandAction(this.phantomCard.makeStatEquivalentCopy(), 1));
    }

    @Override
    public void atStartOfTurn() {
        removeIfBlockGone(this.owner);
    }

    @Override
    public void updateDescription() {
        this.description = this.descriptions[0] + this.blockAmount + this.descriptions[1]
                + this.phantomCard.name + this.descriptions[2];
    }
}
