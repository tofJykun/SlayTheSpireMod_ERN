package general;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.random.Random;
import potions.BulkAttackPotion;
import potions.BulkBlockPotion;
import potions.BulkEnergyPotion;
import potions.BulkExplosivePotion;
import potions.BulkFearPotion;
import potions.BulkFirePotion;
import potions.BulkSwiftPotion;
import potions.BulkWeakenPotion;

import java.util.ArrayList;

public class BulkPotionQueue {
    public static final String SAVE_KEY = "ERNMod:BulkPotionQueue";
    private static final ArrayList<String> BULK_POTION_IDS = new ArrayList<>();
    public static final SaveField SAVE_FIELD = new SaveField();

    private static String roomKey = "";
    private static int index = 0;

    static {
        BULK_POTION_IDS.add(BulkFirePotion.POTION_ID);
        BULK_POTION_IDS.add(BulkEnergyPotion.POTION_ID);
        BULK_POTION_IDS.add(BulkAttackPotion.POTION_ID);
        BULK_POTION_IDS.add(BulkBlockPotion.POTION_ID);
        BULK_POTION_IDS.add(BulkExplosivePotion.POTION_ID);
        BULK_POTION_IDS.add(BulkSwiftPotion.POTION_ID);
        BULK_POTION_IDS.add(BulkFearPotion.POTION_ID);
        BULK_POTION_IDS.add(BulkWeakenPotion.POTION_ID);
    }

    public static AbstractPotion getRandomBulkPotion() {
        String potionId = BULK_POTION_IDS.get(randomIndex(BULK_POTION_IDS.size()));
        return getBulkPotion(potionId);
    }

    public static int randomIndex(int size) {
        if (size <= 0) {
            return 0;
        }
        refreshRoomKey();
        int result = roomRng().random(size - 1);
        index++;
        return result;
    }

    public static AbstractPotion getBulkPotion(String potionId) {
        if (BulkFirePotion.POTION_ID.equals(potionId)) {
            return new BulkFirePotion();
        }
        if (BulkEnergyPotion.POTION_ID.equals(potionId)) {
            return new BulkEnergyPotion();
        }
        if (BulkAttackPotion.POTION_ID.equals(potionId)) {
            return new BulkAttackPotion();
        }
        if (BulkBlockPotion.POTION_ID.equals(potionId)) {
            return new BulkBlockPotion();
        }
        if (BulkExplosivePotion.POTION_ID.equals(potionId)) {
            return new BulkExplosivePotion();
        }
        if (BulkSwiftPotion.POTION_ID.equals(potionId)) {
            return new BulkSwiftPotion();
        }
        if (BulkFearPotion.POTION_ID.equals(potionId)) {
            return new BulkFearPotion();
        }
        if (BulkWeakenPotion.POTION_ID.equals(potionId)) {
            return new BulkWeakenPotion();
        }
        return null;
    }

    public static boolean isBulkPotion(String potionId) {
        return BULK_POTION_IDS.contains(potionId);
    }

    private static Random roomRng() {
        return new Random(Long.valueOf(seedForCurrentRoom()), index);
    }

    private static void refreshRoomKey() {
        String currentKey = currentRoomKey();
        if (!currentKey.equals(roomKey)) {
            roomKey = currentKey;
            index = 0;
        }
    }

    private static String currentRoomKey() {
        long baseSeed = Settings.seed == null ? 0L : Settings.seed.longValue();
        int x = 0;
        int y = 0;
        String roomName = "none";
        MapRoomNode node = AbstractDungeon.currMapNode;
        if (node != null) {
            x = node.x;
            y = node.y;
            if (node.room != null) {
                roomName = node.room.getClass().getName();
            }
        }
        return baseSeed + ":" + AbstractDungeon.floorNum + ":" + x + ":" + y + ":" + roomName;
    }

    private static long seedForCurrentRoom() {
        long seed = Settings.seed == null ? 0L : Settings.seed.longValue();
        MapRoomNode node = AbstractDungeon.currMapNode;
        int x = node == null ? 0 : node.x;
        int y = node == null ? 0 : node.y;
        int roomHash = node == null || node.room == null ? 0 : node.room.getClass().getName().hashCode();
        seed ^= 0x9E3779B97F4A7C15L;
        seed += (long)AbstractDungeon.floorNum * 0xBF58476D1CE4E5B9L;
        seed ^= (long)(x + 31) * 0x94D049BB133111EBL;
        seed += (long)(y + 17) * 0x2545F4914F6CDD1DL;
        seed ^= roomHash;
        return mix(seed);
    }

    private static long mix(long value) {
        value = (value ^ (value >>> 30)) * 0xBF58476D1CE4E5B9L;
        value = (value ^ (value >>> 27)) * 0x94D049BB133111EBL;
        return value ^ (value >>> 31);
    }

    public static class SaveState {
        public String roomKey;
        public int index;
    }

    public static class SaveField implements CustomSavable<SaveState> {
        @Override
        public SaveState onSave() {
            SaveState state = new SaveState();
            state.roomKey = "";
            state.index = 0;
            return state;
        }

        @Override
        public void onLoad(SaveState state) {
            roomKey = "";
            index = 0;
        }
    }
}
