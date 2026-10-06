package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import general.CombatState;
import general.ConciliationRewards;

public class ConciliationAction extends AbstractGameAction {
    private final AbstractRoom room;

    public ConciliationAction(AbstractRoom room) {
        this.room = room;
        actionType = ActionType.SPECIAL;
    }

    public static boolean isEligible(AbstractRoom room) {
        if (room == null || room.phase != AbstractRoom.RoomPhase.COMBAT || room.isBattleOver
                || room.smoked || room instanceof MonsterRoomBoss || room.monsters == null) return false;
        for (AbstractMonster monster : room.monsters.monsters) {
            if (monster.type == AbstractMonster.EnemyType.BOSS) return false;
        }
        return true;
    }

    @Override
    public void update() {
        isDone = true;
        if (CombatState.currentRoom() != room || !isEligible(room) || AbstractDungeon.player == null
                || AbstractDungeon.player.isDead || AbstractDungeon.player.isDying
                || AbstractDungeon.player.currentHealth <= 0 || AbstractDungeon.player.isEscaping) return;
        // Summon callbacks (including Initiative and their follow-up actions) resolve first.
        if (!AbstractDungeon.actionManager.actions.isEmpty()) {
            addToBot(new ConciliationAction(room));
            return;
        }
        boolean unfinished = false;
        for (AbstractMonster monster : room.monsters.monsters) {
            if (monster.halfDead || (!monster.isDeadOrEscaped() && !monster.isDying)) {
                unfinished = true;
                break;
            }
        }
        if (!unfinished) return;

        ConciliationRewards.mark(room);
        AbstractDungeon.effectList.add(new SmokeBombEffect(
                AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY));
        room.cannotLose = false;
        room.skipMonsterTurn = true;
        // Do not set smoked/escaped or kill monsters: keep normal non-card rewards.
        room.endBattle();
    }
}
