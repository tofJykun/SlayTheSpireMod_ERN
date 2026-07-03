package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import powers.IntelligencePower;

public class DoubleIntelligenceAction extends AbstractGameAction {
    @Override
    public void update() {
        if (AbstractDungeon.player.hasPower(IntelligencePower.POWER_ID)) {
            int intelligence = AbstractDungeon.player.getPower(IntelligencePower.POWER_ID).amount;
            addToTop(new ApplyPowerAction((AbstractCreature)AbstractDungeon.player,
                    (AbstractCreature)AbstractDungeon.player,
                    new IntelligencePower((AbstractCreature)AbstractDungeon.player, intelligence),
                    intelligence));
        }
        this.isDone = true;
    }
}
