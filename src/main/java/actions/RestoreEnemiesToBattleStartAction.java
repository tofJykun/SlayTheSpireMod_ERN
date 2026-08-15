package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import general.EnemyBattleStartSnapshot;

public class RestoreEnemiesToBattleStartAction extends AbstractGameAction {
    @Override
    public void update() {
        EnemyBattleStartSnapshot.restore();
        this.isDone = true;
    }
}
