package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class WhiteLightningPower extends AbstractPower {
    public static final String POWER_ID = "WhiteLightningPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static int powerIdOffset;

    private final int damage;

    public WhiteLightningPower(AbstractCreature owner, int turns, int damage) {
        this.name = NAME;
        this.ID = POWER_ID + powerIdOffset;
        powerIdOffset++;
        this.owner = owner;
        this.amount = turns;
        this.damage = damage;
        this.type = PowerType.BUFF;
        loadRegion("the_bomb");
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            addToBot((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, this, 1));
            if (this.amount == 1) {
                addToBot((AbstractGameAction)new DamageAllEnemiesAction(null,
                        DamageInfo.createDamageMatrix(this.damage, true), DamageInfo.DamageType.THORNS,
                        AbstractGameAction.AttackEffect.LIGHTNING));
            }
        }
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = String.format(DESCRIPTIONS[1], Integer.valueOf(this.damage));
        } else {
            this.description = String.format(DESCRIPTIONS[0], Integer.valueOf(this.amount),
                    Integer.valueOf(this.damage));
        }
    }
}
