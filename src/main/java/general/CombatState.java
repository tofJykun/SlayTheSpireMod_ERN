package general;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class CombatState {
    private CombatState() {
    }

    public static AbstractRoom currentRoom() {
        if (AbstractDungeon.getCurrMapNode() == null) {
            return null;
        }
        return AbstractDungeon.getCurrRoom();
    }

    public static boolean isInCombat() {
        AbstractRoom room = currentRoom();
        return room != null && room.phase == AbstractRoom.RoomPhase.COMBAT;
    }
}
