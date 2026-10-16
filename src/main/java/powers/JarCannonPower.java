package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CombatState;

public class JarCannonPower extends AbstractPower {
    public static final String POWER_ID = "JarCannonPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static long nextId;
    private final AbstractMonster recipient;
    private final int savedDamage;
    private boolean triggered;

    public JarCannonPower(AbstractCreature owner, AbstractMonster recipient, int damage) {
        this.ID = POWER_ID + nextId++;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.recipient = recipient;
        this.amount = this.savedDamage = damage;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    private boolean validTarget() {
        return CombatState.isInCombat() && !owner.isDying && owner.currentHealth > 0
                && recipient != null && !recipient.isDeadOrEscaped()
                && !recipient.halfDead && recipient.currentHealth > 0;
    }

    @Override
    public void atStartOfTurn() {
        if (triggered) return;
        triggered = true;
        flash();
        addToBot(new RemoveSpecificPowerAction(owner, owner, this.ID));
        if (validTarget()) {
            addToBot(new DamageAction(recipient, new DamageInfo(owner, savedDamage, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.BLUNT_HEAVY) {
                @Override
                public void update() {
                    // Other start-of-turn actions can invalidate the original target.
                    if (!validTarget()) {
                        this.isDone = true;
                        return;
                    }
                    super.update();
                }
            });
        }
    }

    @Override
    public void updateDescription() {
        this.description = String.format(STRINGS.DESCRIPTIONS[0], recipient.name, savedDamage);
    }
}
