package powers;

import cards.tempcards.AbstractPhantomCard;
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
    private final AbstractPhantomCard phantomCard;
    private final String summonKey;
    private int displayedBlock;

    protected AbstractSummonPower(AbstractCreature owner, String powerId, String name, String[] descriptions,
                                  int blockAmount, AbstractPhantomCard phantomCard, String summonKey) {
        this.ID = powerId;
        this.name = name;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        this.descriptions = descriptions;
        this.blockAmount = blockAmount;
        this.phantomCard = phantomCard;
        this.summonKey = summonKey;
        PowerIconHelper.load(this, this.ID);
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

    public String getSummonKey() {
        return this.summonKey;
    }

    public abstract AbstractSummonPower makeSummonCopy(AbstractCreature owner);

    public static AbstractSummonPower getActiveSummon(AbstractCreature creature) {
        if (creature == null) {
            return null;
        }
        AbstractSummonPower active = null;
        for (AbstractPower power : creature.powers) {
            if (isSummonPower(power)) {
                active = (AbstractSummonPower)power;
            }
        }
        return active;
    }

    public static boolean hasSummonPower(AbstractCreature creature) {
        return getActiveSummon(creature) != null;
    }

    public static boolean isActiveSummon(AbstractCreature creature, String powerId) {
        AbstractSummonPower active = getActiveSummon(creature);
        return active != null && active.ID.equals(powerId);
    }

    public static boolean isSummonPower(AbstractPower power) {
        return power instanceof AbstractSummonPower;
    }

    public static AbstractCard makeActiveSummonPhantomCard(AbstractCreature creature) {
        AbstractSummonPower active = getActiveSummon(creature);
        return active == null ? null : active.makePhantomCardCopy();
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
            if (isSummonPower(power) && power != this) {
                toRemove.add(power);
            }
        }
        for (AbstractPower power : toRemove) {
            AbstractDungeon.actionManager.addToTop((AbstractGameAction)new RemoveSpecificPowerAction(
                    this.owner, this.owner, power));
        }
    }

    private void triggerSummonEffect() {
        addToBot((AbstractGameAction)new GainBlockAction(this.owner, this.owner,
                SpiritPower.summonBlock(this.owner, this.blockAmount)));
        addToBot((AbstractGameAction)new MakeTempCardInHandAction(this.phantomCard.makeStatEquivalentCopy(), 1));
    }

    private AbstractCard makePhantomCardCopy() {
        return this.phantomCard.makeStatEquivalentCopy();
    }

    @Override
    public void atStartOfTurn() {
        removeIfBlockGone(this.owner);
    }

    @Override
    public void updateDescription() {
        this.displayedBlock = SpiritPower.summonBlock(this.owner, this.blockAmount);
        this.description = this.descriptions[0] + this.displayedBlock + this.descriptions[1]
                + this.phantomCard.name + this.descriptions[2];
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        if (this.displayedBlock != SpiritPower.summonBlock(this.owner, this.blockAmount)) {
            updateDescription();
        }
    }
}

