package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;

/** Gains player gold and emits the standard coin gain feedback. */
public class GainGoldWithAnimationAction extends AbstractGameAction {
    private final int amount;

    public GainGoldWithAnimationAction(int amount) {
        this.amount = Math.max(0, amount);
    }

    @Override
    public void update() {
        if (amount > 0 && AbstractDungeon.player != null) {
            AbstractDungeon.player.gainGold(amount);
            for (int i = 0; i < amount; i++) {
                AbstractDungeon.effectList.add(new GainPennyEffect(
                        AbstractDungeon.player,
                        AbstractDungeon.player.hb.cX,
                        AbstractDungeon.player.hb.cY,
                        AbstractDungeon.player.hb.cX,
                        AbstractDungeon.player.hb.cY,
                        true));
            }
        }
        isDone = true;
    }
}
