package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import powers.BloodburnPower;
import powers.ScarletRotPower;

import java.util.ArrayList;

/** Replays the normal start-of-turn trigger once for each enemy anomaly. */
public class TriggerEnemyAnomaliesAction extends AbstractGameAction {
    public TriggerEnemyAnomaliesAction() {
        this.actionType = ActionType.POWER;
    }

    @Override
    public void update() {
        if (AbstractDungeon.getMonsters() == null) {
            this.isDone = true;
            return;
        }

        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster.isDeadOrEscaped()) {
                continue;
            }
            for (AbstractPower power : new ArrayList<>(monster.powers)) {
                if (power instanceof PoisonPower
                        || power instanceof ScarletRotPower
                        || power instanceof BloodburnPower) {
                    power.atStartOfTurn();
                }
            }
        }
        this.isDone = true;
    }
}
