package actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RandomDamageWithPowersAction extends AbstractGameAction {
    private final AbstractCreature source;
    private final AbstractCard card;
    private final int baseDamage;
    private final DamageInfo.DamageType damageType;
    private final boolean applySourcePowers;

    public RandomDamageWithPowersAction(AbstractCreature source, AbstractCard card, int baseDamage,
                                        DamageInfo.DamageType damageType, AttackEffect effect) {
        this(source, card, baseDamage, damageType, effect, true);
    }

    public RandomDamageWithPowersAction(AbstractCreature source, AbstractCard card, int baseDamage,
                                        DamageInfo.DamageType damageType, AttackEffect effect,
                                        boolean applySourcePowers) {
        this.source = source;
        this.card = card;
        this.baseDamage = baseDamage;
        this.damageType = damageType;
        this.applySourcePowers = applySourcePowers;
        this.attackEffect = effect;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        AbstractMonster monster = AbstractDungeon.getMonsters().getRandomMonster(null, true,
                AbstractDungeon.cardRandomRng);
        if (monster != null) {
            addToTop(new DamageAction(monster, makeDamageInfo(monster), this.attackEffect));
        }
        this.isDone = true;
    }

    private DamageInfo makeDamageInfo(AbstractCreature target) {
        DamageInfo info = new DamageInfo(this.source, this.baseDamage, this.damageType);
        info.output = calculateDamage(target);
        info.isModified = info.output != this.baseDamage;
        return info;
    }

    private int calculateDamage(AbstractCreature target) {
        float tmp = this.baseDamage;

        if (!this.applySourcePowers) {
            return calculateTargetDamage(target, tmp);
        }

        if (this.source.isPlayer) {
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                tmp = relic.atDamageModify(tmp, this.card);
            }

            for (AbstractPower power : this.source.powers) {
                tmp = power.atDamageGive(tmp, this.damageType);
            }

            tmp = AbstractDungeon.player.stance.atDamageGive(tmp, this.damageType, this.card);

            for (AbstractPower power : target.powers) {
                tmp = power.atDamageReceive(tmp, this.damageType);
            }

            for (AbstractPower power : this.source.powers) {
                tmp = power.atDamageFinalGive(tmp, this.damageType);
            }

            for (AbstractPower power : target.powers) {
                tmp = power.atDamageFinalReceive(tmp, this.damageType);
            }
        } else {
            for (AbstractPower power : this.source.powers) {
                tmp = power.atDamageGive(tmp, this.damageType);
            }

            for (AbstractPower power : target.powers) {
                tmp = power.atDamageReceive(tmp, this.damageType);
            }

            tmp = AbstractDungeon.player.stance.atDamageReceive(tmp, this.damageType);

            for (AbstractPower power : this.source.powers) {
                tmp = power.atDamageFinalGive(tmp, this.damageType);
            }

            for (AbstractPower power : target.powers) {
                tmp = power.atDamageFinalReceive(tmp, this.damageType);
            }
        }

        if (tmp < 0.0F) {
            tmp = 0.0F;
        }
        return MathUtils.floor(tmp);
    }

    private int calculateTargetDamage(AbstractCreature target, float damage) {
        float tmp = damage;
        for (AbstractPower power : target.powers) {
            tmp = power.atDamageReceive(tmp, this.damageType);
        }
        for (AbstractPower power : target.powers) {
            tmp = power.atDamageFinalReceive(tmp, this.damageType);
        }
        if (tmp < 0.0F) {
            tmp = 0.0F;
        }
        return MathUtils.floor(tmp);
    }
}
