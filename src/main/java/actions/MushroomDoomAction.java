package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;

public class MushroomDoomAction extends AbstractGameAction {
    private static final int DAMAGE = 99999;
    private final AbstractPower powerToRemove;

    public MushroomDoomAction(AbstractCreature source, AbstractPower powerToRemove) {
        this.source = source;
        this.powerToRemove = powerToRemove;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        if (target != null) {
            addToTop((AbstractGameAction)new RemoveSpecificPowerAction(this.source, this.source, this.powerToRemove));
            addToTop((AbstractGameAction)new DamageAction(target,
                    new DamageInfo(this.source, DAMAGE, DamageInfo.DamageType.THORNS),
                    AttackEffect.NONE));
            addToTop((AbstractGameAction)new WaitAction(0.8F));
            addToTop((AbstractGameAction)new VFXAction(new WeightyImpactEffect(target.hb.cX, target.hb.cY)));
        } else {
            addToTop((AbstractGameAction)new RemoveSpecificPowerAction(this.source, this.source, this.powerToRemove));
        }
        this.isDone = true;
    }
}
