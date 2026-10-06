package general;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class ConciliationRewards implements CustomSavable<Integer> {
    public static final String SAVE_KEY = "ERN:ConciliationRewards";
    private static int floor = -1;
    private static AbstractRoom finishedRoom;

    public static void mark(AbstractRoom room) {
        finishedRoom = room;
        floor = AbstractDungeon.floorNum;
    }

    public static boolean suppressCards(AbstractRoom room) {
        return room != null && (room.smoked || (floor == AbstractDungeon.floorNum
                && (finishedRoom == null || finishedRoom == room)));
    }

    public static void reset() {
        floor = -1;
        finishedRoom = null;
    }

    @Override
    public Integer onSave() {
        return floor == AbstractDungeon.floorNum ? floor : -1;
    }

    @Override
    public void onLoad(Integer savedFloor) {
        floor = savedFloor == null ? -1 : savedFloor;
        finishedRoom = null;
    }
}
