package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import general.GuidanceHelper;

public class SeekGuidanceAction extends AbstractGameAction {
    @Override
    public void update() {
        if (AbstractDungeon.player != null) {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX,
                    AbstractDungeon.player.dialogY, 3.0F, GuidanceHelper.randomMessage(), true));
        }
        this.isDone = true;
    }
}
