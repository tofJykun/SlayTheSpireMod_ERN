package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import java.util.ArrayList;
import monsters.MiniatureStraghessSlimeBoss;

public class MiniatureStraghessAction extends AbstractGameAction {
    private final AbstractMonster targetMonster;

    public MiniatureStraghessAction(AbstractMonster targetMonster) {
        this.targetMonster = targetMonster;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (this.targetMonster == null || this.targetMonster.isDeadOrEscaped()) {
            this.isDone = true;
            return;
        }

        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if (room == null || room.monsters == null || room.monsters.monsters == null) {
            this.isDone = true;
            return;
        }

        int index = room.monsters.monsters.indexOf(this.targetMonster);
        if (index < 0) {
            this.isDone = true;
            return;
        }

        int copies = Math.max(1, AbstractDungeon.actNum);
        int maxHp = Math.max(1, this.targetMonster.maxHealth);
        float baseOffsetX = (this.targetMonster.drawX - Settings.WIDTH * 0.75F) / Settings.xScale;
        float baseOffsetY = (this.targetMonster.drawY - AbstractDungeon.floorY) / Settings.yScale;
        float spacing = 260.0F;
        float start = -spacing * (copies - 1) / 2.0F;

        ArrayList<AbstractMonster> next = new ArrayList<>();
        for (int i = 0; i < room.monsters.monsters.size(); i++) {
            AbstractMonster monster = room.monsters.monsters.get(i);
            if (i == index) {
                for (int j = 0; j < copies; j++) {
                    MiniatureStraghessSlimeBoss slime = new MiniatureStraghessSlimeBoss(maxHp, baseOffsetX + start + spacing * j, baseOffsetY);
                    slime.init();
                    next.add(slime);
                }
            } else {
                next.add(monster);
            }
        }

        this.targetMonster.hideHealthBar();
        room.monsters = new MonsterGroup(next.toArray(new AbstractMonster[0]));
        room.monsters.showIntent();
        this.isDone = true;
    }
}
