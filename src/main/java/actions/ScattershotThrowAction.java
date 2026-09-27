package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.CombatState;
import general.PotionHistory;

public class ScattershotThrowAction extends AbstractGameAction {
    private final String potionId;
    private final AbstractMonster preferredTarget;
    private final int remaining;

    public ScattershotThrowAction(String potionId, AbstractMonster preferredTarget, int remaining) {
        this.potionId = potionId;
        this.preferredTarget = preferredTarget;
        this.remaining = remaining;
    }

    @Override
    public void update() {
        isDone = true;
        if (remaining <= 0 || !CombatState.isInCombat()
                || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            return;
        }
        PotionHistory.replayPotion(potionId, preferredTarget, false);
        // Append after this use's actions, so choice potions finish before the next copy starts.
        if (remaining > 1) {
            addToBot(new ScattershotThrowAction(potionId, preferredTarget, remaining - 1));
        }
    }
}
