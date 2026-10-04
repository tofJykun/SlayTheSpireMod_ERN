package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import general.CombatState;
import patches.NineStagesOfDecayRewardPatch;

import java.util.ArrayList;

public class NineStagesOfDecayAction extends AbstractGameAction {
    private final AbstractRoom room;

    public NineStagesOfDecayAction(AbstractRoom room) {
        this.room = room;
        this.actionType = ActionType.SPECIAL;
    }

    public static boolean isEligible(AbstractRoom room) {
        return room != null && room.phase == AbstractRoom.RoomPhase.COMBAT
                && !room.isBattleOver && !(room instanceof MonsterRoomBoss);
    }

    @Override
    public void update() {
        isDone = true;
        if (CombatState.currentRoom() != room || !isEligible(room) || room.monsters == null
                || AbstractDungeon.player == null || AbstractDungeon.player.isDead || AbstractDungeon.player.isDying) {
            return;
        }
        boolean unfinishedEnemy = false;
        for (AbstractMonster monster : room.monsters.monsters) {
            if (monster.halfDead || (!monster.isDeadOrEscaped() && !monster.isDying)) {
                unfinishedEnemy = true;
                break;
            }
        }
        if (!unfinishedEnemy) {
            return;
        }

        room.smoked = true;
        room.mugged = false;
        room.cannotLose = false;
        room.skipMonsterTurn = true;
        NineStagesOfDecayRewardPatch.clearRewards(room, AbstractDungeon.combatRewardScreen);
        for (AbstractMonster monster : new ArrayList<>(room.monsters.monsters)) {
            if (monster.isDeadOrEscaped() && !monster.halfDead) {
                continue;
            }
            monster.currentHealth = 0;
            monster.halfDead = false;
            monster.healthBarUpdatedEvent();
            monster.hideHealthBar();
            // Bypass onDeath summons/rebirth and finish both stages, not merely the current health bar.
            monster.die(false);
            monster.halfDead = false;
            monster.isDying = true;
            monster.isDead = true;
            monster.powers.clear();
        }
        room.endBattle();
        NineStagesOfDecayRewardPatch.clearRewards(room, AbstractDungeon.combatRewardScreen);
    }
}
