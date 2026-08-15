package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class BloodbiteAction extends AbstractGameAction {
    private static final int HEALTH_PER_STRENGTH = 3;
    private final AbstractPlayer player;

    public BloodbiteAction(AbstractPlayer player) {
        this.player = player;
    }

    @Override
    public void update() {
        int previousHealth = this.player.currentHealth;
        int targetHealth = (this.player.maxHealth + 1) / 2;

        // Direct assignment deliberately bypasses both healing and damage modifiers.
        this.player.currentHealth = targetHealth;
        this.player.healthBarUpdatedEvent();

        int healthChange = targetHealth - previousHealth;
        int strengthChange = Math.abs(healthChange) / HEALTH_PER_STRENGTH;
        if (strengthChange > 0) {
            int amount = healthChange > 0 ? strengthChange : -strengthChange;
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (!monster.isDeadOrEscaped() && !monster.isDying && !monster.halfDead) {
                    addToTop(new ApplyPowerAction(monster, this.player,
                            new StrengthPower(monster, amount), amount));
                }
            }
        }

        this.isDone = true;
    }
}
