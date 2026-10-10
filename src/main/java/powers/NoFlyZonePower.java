package powers;

import actions.BloodburnLoseHpAction;
import actions.BloodlossReduceMaxHealthAction;
import actions.SolderingIronAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CombatState;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;

public class NoFlyZonePower extends AbstractPower {
    public static final String POWER_ID = "NoFlyZonePower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final Set<AbstractMonster> pending = Collections.newSetFromMap(new IdentityHashMap<AbstractMonster, Boolean>());

    public NoFlyZonePower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = -1;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount = -1;
    }

    @Override
    public void updateDescription() {
        this.description = STRINGS.DESCRIPTIONS[0];
    }

    private static NoFlyZonePower activePower() {
        if (!CombatState.isInCombat() || AbstractDungeon.player == null) return null;
        AbstractPower power = AbstractDungeon.player.getPower(POWER_ID);
        return power instanceof NoFlyZonePower ? (NoFlyZonePower)power : null;
    }

    public static void onEnemyLoseHp(AbstractMonster monster, int loss) {
        if (loss <= 0) return;
        NoFlyZonePower power = activePower();
        if (power != null) power.pending.add(monster);
    }

    public static void finishHealthChanges() {
        NoFlyZonePower power = activePower();
        if (power == null || AbstractDungeon.actionManager == null) return;
        boolean changed = false;
        Iterator<AbstractMonster> iterator = power.pending.iterator();
        while (iterator.hasNext()) {
            AbstractMonster monster = iterator.next();
            if (monster.isDeadOrEscaped() || monster.halfDead || monster.currentHealth <= 0) {
                iterator.remove();
                continue;
            }
            if (hasPendingMaxHealthLoss(monster)) continue;
            iterator.remove();
            if (monster.maxHealth > monster.currentHealth) {
                // Do not call decreaseMaxHealth: alignment must not inflict another HP loss.
                monster.maxHealth = monster.currentHealth;
                monster.healthBarUpdatedEvent();
                changed = true;
            }
        }
        if (changed) power.flash();
    }

    private static boolean hasPendingMaxHealthLoss(AbstractMonster monster) {
        if (reducesMaxHealth(AbstractDungeon.actionManager.currentAction, monster)) return true;
        for (AbstractGameAction action : AbstractDungeon.actionManager.actions) {
            if (reducesMaxHealth(action, monster)) return true;
        }
        for (AbstractGameAction action : AbstractDungeon.actionManager.preTurnActions) {
            if (reducesMaxHealth(action, monster)) return true;
        }
        return false;
    }

    private static boolean reducesMaxHealth(AbstractGameAction action, AbstractMonster monster) {
        // Bloodloss splits HP and max-HP loss across actions; wait for both halves.
        return action != null && !action.isDone && action.target == monster
                && (action instanceof BloodlossReduceMaxHealthAction
                    || action instanceof BloodburnLoseHpAction || action instanceof SolderingIronAction);
    }
}
