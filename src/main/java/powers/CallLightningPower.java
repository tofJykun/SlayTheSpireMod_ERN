package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class CallLightningPower extends AbstractPower {
    public static final String POWER_ID = "CallLightningPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public CallLightningPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            return;
        }

        flash();
        addToBot((AbstractGameAction)new DamageAllEnemiesAction(null,
                DamageInfo.createDamageMatrix(this.amount, true), DamageInfo.DamageType.THORNS,
                AbstractGameAction.AttackEffect.LIGHTNING));
        if (this.owner.hasPower(LoftyPower.POWER_ID)) {
            addToBot((AbstractGameAction)new DamageAction(this.owner,
                    new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.LIGHTNING));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]
                + this.amount + DESCRIPTIONS[2];
    }
}
