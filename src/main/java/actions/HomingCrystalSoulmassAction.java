package actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class HomingCrystalSoulmassAction extends AbstractGameAction {
    private final AbstractCreature source;
    private final AbstractCard card;
    private final int baseDamage;
    private final int hits;

    public HomingCrystalSoulmassAction(AbstractCreature source, AbstractCard card, int baseDamage, int hits) {
        this.source = source;
        this.card = card;
        this.baseDamage = baseDamage;
        this.hits = hits;
    }

    @Override
    public void update() {
        if (this.isDone) {
            return;
        }
        this.isDone = true;
        final Set<AbstractMonster> hitEnemies = Collections.newSetFromMap(
                new IdentityHashMap<AbstractMonster, Boolean>());
        addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                if (!hitEnemies.isEmpty()) {
                    addToTop(new GainEnergyAction(hitEnemies.size()));
                }
                this.isDone = true;
            }
        });
        for (int i = 0; i < this.hits; i++) {
            addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    AbstractMonster monster = AbstractDungeon.getMonsters().getRandomMonster(
                            null, true, AbstractDungeon.cardRandomRng);
                    if (monster != null) {
                        hitEnemies.add(monster);
                        addToTop(new DamageAction(monster, makeDamageInfo(monster), AttackEffect.BLUNT_HEAVY));
                    }
                    this.isDone = true;
                }
            });
        }
    }

    private DamageInfo makeDamageInfo(AbstractMonster target) {
        DamageInfo info = new DamageInfo(this.source, this.baseDamage, this.card.damageTypeForTurn);
        info.output = calculateDamage(target);
        info.isModified = info.output != this.baseDamage;
        return info;
    }

    private int calculateDamage(AbstractMonster target) {
        float tmp = this.baseDamage;
        if (this.source.isPlayer) {
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                tmp = relic.atDamageModify(tmp, this.card);
            }
            for (AbstractPower power : this.source.powers) {
                tmp = power.atDamageGive(tmp, this.card.damageTypeForTurn, this.card);
            }
            tmp = AbstractDungeon.player.stance.atDamageGive(tmp, this.card.damageTypeForTurn, this.card);
            for (AbstractPower power : target.powers) {
                tmp = power.atDamageReceive(tmp, this.card.damageTypeForTurn);
            }
            for (AbstractPower power : this.source.powers) {
                tmp = power.atDamageFinalGive(tmp, this.card.damageTypeForTurn, this.card);
            }
            for (AbstractPower power : target.powers) {
                tmp = power.atDamageFinalReceive(tmp, this.card.damageTypeForTurn);
            }
        }
        return MathUtils.floor(Math.max(0.0F, tmp));
    }
}
