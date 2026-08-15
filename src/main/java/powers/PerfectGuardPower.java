package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class PerfectGuardPower extends AbstractPower {
    public static final String POWER_ID = "PerfectGuardPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int VIGOR = 6;

    public PerfectGuardPower(AbstractCreature owner, int amount) {
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

    public static void triggerIfPerfectGuard(AbstractPlayer player, AbstractMonster attacker) {
        if (player == null || !player.hasPower(POWER_ID) || player.currentBlock < 15) {
            return;
        }

        AbstractPower power = player.getPower(POWER_ID);
        if (power != null) {
            power.flash();
        }
        AbstractDungeon.actionManager.addToTop((AbstractGameAction)new ApplyPowerAction((AbstractCreature)player, (AbstractCreature)player,
                new VigorPower((AbstractCreature)player, VIGOR), VIGOR));
        notifyBarricadeShield(player, attacker);
    }

    private static void notifyBarricadeShield(AbstractPlayer player, AbstractMonster attacker) {
        AbstractPower barricadeShield = player.getPower(BarricadeShieldPower.POWER_ID);
        if (barricadeShield instanceof BarricadeShieldPower) {
            ((BarricadeShieldPower)barricadeShield).onPerfectGuard(attacker);
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
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + VIGOR + DESCRIPTIONS[2];
    }
}

