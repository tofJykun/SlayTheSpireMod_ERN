package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ParryPower extends AbstractPower {
    public static final String POWER_ID = "ParryPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ParryPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateDescription();
    }

    public static void triggerIfParry(AbstractPlayer player, DamageInfo info, int damageAmount) {
        if (player == null || info == null || !player.hasPower(POWER_ID)
                || !(info.owner instanceof AbstractMonster) || info.type != DamageInfo.DamageType.NORMAL
                || damageAmount <= 0 || player.currentBlock != damageAmount) {
            return;
        }

        AbstractPower power = player.getPower(POWER_ID);
        if (power != null) {
            power.flash();
        }

        AbstractMonster monster = (AbstractMonster)info.owner;
        if (!monster.hasPower(DelayedStunPower.POWER_ID) && !monster.hasPower(StunPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToTop((AbstractGameAction)new ApplyPowerAction(
                    (AbstractCreature)monster, (AbstractCreature)player,
                    new DelayedStunPower(monster, 1), 1));
            AbstractPower ceruleanDagger = player.getPower(CeruleanDaggerPower.POWER_ID);
            if (ceruleanDagger instanceof CeruleanDaggerPower) {
                ((CeruleanDaggerPower)ceruleanDagger).onSuccessfulParry();
            }
        }
    }

    @Override
    public void atStartOfTurn() {
        if (this.amount <= 1) {
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            addToBot((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}

