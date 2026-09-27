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

public class EarthshakerPower extends AbstractPower {
    public static final String POWER_ID = "EarthshakerPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static int powerIdOffset;

    private final int[] damage;

    public EarthshakerPower(AbstractCreature owner, int turns, int[] damage) {
        this.name = POWER_STRINGS.NAME;
        this.ID = POWER_ID + powerIdOffset++;
        this.owner = owner;
        this.amount = turns;
        this.damage = damage == null ? new int[0] : damage.clone();
        this.type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            return;
        }

        if (this.amount == 1) {
            addToBot((AbstractGameAction)new DamageAllEnemiesAction(this.owner, this.damage.clone(),
                    DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
        addToBot((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, this, 1));
    }

    @Override
    public void updateDescription() {
        int displayDamage = this.damage.length == 0 ? 0 : this.damage[0];
        if (this.amount == 1) {
            this.description = String.format(POWER_STRINGS.DESCRIPTIONS[1], displayDamage);
        } else {
            this.description = String.format(POWER_STRINGS.DESCRIPTIONS[0], this.amount, displayDamage);
        }
    }
}
