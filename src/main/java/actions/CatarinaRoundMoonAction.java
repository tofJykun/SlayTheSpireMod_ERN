package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.GreyHealthPlusPower;
import powers.GreyHealthPower;

public class CatarinaRoundMoonAction extends AbstractGameAction {
    private final AbstractCreature owner;

    public CatarinaRoundMoonAction(AbstractCreature owner) {
        this.owner = owner;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        this.isDone = true;
        if (this.owner == null) {
            return;
        }
        AbstractPower greyHealth = this.owner.getPower(GreyHealthPower.POWER_ID);
        AbstractPower greyHealthPlus = this.owner.getPower(GreyHealthPlusPower.POWER_ID);
        long total = (long)positiveAmount(greyHealth) + positiveAmount(greyHealthPlus);

        // Top-queue in reverse order: remove both powers before damage or the next pending moon.
        if (total > 0) {
            addToTop(new DamageAllEnemiesAction(null,
                    DamageInfo.createDamageMatrix((int)Math.min(Integer.MAX_VALUE, total), true),
                    DamageInfo.DamageType.THORNS, AttackEffect.FIRE));
        }
        if (greyHealthPlus != null) {
            addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, greyHealthPlus));
        }
        if (greyHealth != null) {
            addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, greyHealth));
        }
    }

    private static int positiveAmount(AbstractPower power) {
        return power == null ? 0 : Math.max(0, power.amount);
    }
}
