package general;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.*;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

/** Per-node content, separate from reward RNG and from the player's run state. */
public class LordvesselHistory implements CustomSavable<LordvesselHistory.State> {
    public static final String SAVE_KEY = "ERN:LordvesselRooms";
    public static class State {
        public Map<String, RoomData> rooms = new HashMap<>();
        public String activeKey;
        public boolean activeReplay;
        public ArrayList<Jump> jumps = new ArrayList<>();
    }
    public static class Jump {
        public String act;
        public int fromX, fromY, toX, toY;
        public float fromOffsetX, fromOffsetY, toOffsetX, toOffsetY;
        public boolean boss;
    }
    public static class RoomData {
        public String roomClass;
        public String symbol;
        public String eventId;
        public String encounter;
        public long[] encounterRng;
        public Map<String, ArrayList<String>> random = new HashMap<>();
        public Map<String, ArrayList<ArrayList<CardData>>> cards = new HashMap<>();
    }
    public static class CardData {
        public String id;
        public int upgrades;
        public int misc;
        public CardData() {}
        CardData(AbstractCard card) {
            id = card.cardID;
            upgrades = card.timesUpgraded;
            misc = card.misc;
        }
        AbstractCard create() { return CardLibrary.getCopy(id, upgrades, misc); }
    }
    private static final Gson JSON = new Gson();
    private static State state = new State();
    private static RoomData current;
    private static boolean replay;
    private static boolean leavingReplay;
    private static int scope;
    private static final ArrayDeque<Integer> chestScopes = new ArrayDeque<>();
    private static final Map<String, Integer> positions = new HashMap<>();

    public static void reset() {
        state = new State();
        current = null;
        replay = leavingReplay = false;
        scope = 0;
        chestScopes.clear();
        positions.clear();
    }

    private static String key(MapRoomNode node) {
        // Ascension 20's second Boss is a new fight, not a revisit of the first.
        String position = node.room instanceof MonsterRoomBoss ? "boss:" + AbstractDungeon.bossKey : node.x + ":" + node.y;
        return AbstractDungeon.actNum + ":" + AbstractDungeon.id + ":" + position;
    }

    public static void recordJump(MapRoomNode from, MapRoomNode to) {
        Jump jump = new Jump();
        jump.act = AbstractDungeon.actNum + ":" + AbstractDungeon.id;
        jump.fromX = from.x;
        jump.fromY = from.y;
        jump.fromOffsetX = from.offsetX;
        jump.fromOffsetY = from.offsetY;
        jump.toX = to.x;
        jump.toY = to.y;
        jump.toOffsetX = to.offsetX;
        jump.toOffsetY = to.offsetY;
        jump.boss = to.room instanceof MonsterRoomBoss;
        state.jumps.add(jump);
    }

    public static ArrayList<Jump> jumps() { return state.jumps; }

    public static boolean hasBacktrackedInAct() {
        String act = AbstractDungeon.actNum + ":" + AbstractDungeon.id;
        for (Jump jump : state.jumps) {
            if (act.equals(jump.act) && jump.toY < jump.fromY) return true;
        }
        return false;
    }

    public static void prepare() {
        leavingReplay = state.activeReplay;
        MapRoomNode node = AbstractDungeon.nextRoom;
        current = null;
        scope = 0;
        positions.clear();
        if (!LordvesselRoutes.isMapRoom(node)) {
            state.activeKey = null;
            state.activeReplay = false;
            return;
        }
        String key = key(node);
        current = state.rooms.get(key);
        replay = current != null;
        if (CardCrawlGame.loadingSave && key.equals(state.activeKey)) replay = state.activeReplay;
        if (current == null) {
            current = new RoomData();
            current.roomClass = node.room.getClass().getName();
            current.symbol = node.room.getMapSymbol();
            state.rooms.put(key, current);
        } else {
            // Never reuse a disposed room, completed event or dead MonsterGroup.
            try {
                node.room = (AbstractRoom) Class.forName(current.roomClass).getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Cannot restore Lordvessel room " + current.roomClass, e);
            }
            if ("?".equals(current.symbol)) {
                node.room.setMapSymbol("?");
                node.room.setMapImg(ImageMaster.MAP_NODE_EVENT, ImageMaster.MAP_NODE_EVENT_OUTLINE);
                node.room.combatEvent = node.room instanceof MonsterRoom;
            }
        }
        state.activeKey = key;
        state.activeReplay = replay;
    }

    public static Object removeEncounter(ArrayList<?> list, int index) {
        if (leavingReplay && (list == AbstractDungeon.monsterList || list == AbstractDungeon.eliteMonsterList)) {
            return list.get(index);
        }
        if (replay && list == AbstractDungeon.bossList) return index < list.size() ? list.get(index) : null;
        return list.remove(index);
    }

    public static EventHelper.RoomResult rollRoom(Random rng) {
        if (current != null && current.eventId != null) return EventHelper.RoomResult.EVENT;
        return EventHelper.roll(rng);
    }

    public static void enter(AbstractRoom room) {
        if (current != null) {
            current.roomClass = room.getClass().getName();
            current.symbol = room.getMapSymbol();
        }
        boolean fixedContent = room instanceof ShopRoom || room instanceof TreasureRoom;
        if (fixedContent) scope++;
        try {
            room.onPlayerEntry();
        } finally {
            if (fixedContent) scope--;
        }
    }

    public static MonsterGroup encounter(boolean elite) {
        if (current != null && current.encounter != null) {
            AbstractDungeon.lastCombatMetricKey = current.encounter;
            return recreateEncounter();
        }
        rememberEncounterRng();
        MonsterGroup result = elite ? CardCrawlGame.dungeon.getEliteMonsterForRoomCreation()
                : CardCrawlGame.dungeon.getMonsterForRoomCreation();
        if (current != null) current.encounter = AbstractDungeon.lastCombatMetricKey;
        return result;
    }

    public static MonsterGroup boss() {
        if (current != null && current.encounter != null) {
            AbstractDungeon.lastCombatMetricKey = current.encounter;
            AbstractDungeon.dungeonMapScreen.map.atBoss = true;
            return recreateEncounter();
        }
        rememberEncounterRng();
        MonsterGroup result = CardCrawlGame.dungeon.getBoss();
        if (current != null) current.encounter = AbstractDungeon.lastCombatMetricKey;
        return result;
    }

    private static void rememberEncounterRng() {
        if (current != null) current.encounterRng = new long[] {
                AbstractDungeon.miscRng.random.getState(0), AbstractDungeon.miscRng.random.getState(1),
                AbstractDungeon.miscRng.counter};
    }

    private static MonsterGroup recreateEncounter() {
        Random original = AbstractDungeon.miscRng;
        if (current.encounterRng != null) {
            Random fixed = new Random(0L);
            fixed.random.setState(current.encounterRng[0], current.encounterRng[1]);
            fixed.counter = (int) current.encounterRng[2];
            AbstractDungeon.miscRng = fixed;
        }
        try {
            return MonsterHelper.getEncounter(current.encounter);
        } finally {
            AbstractDungeon.miscRng = original;
        }
    }

    public static AbstractEvent generateEvent(Random rng) {
        scope++;
        try {
            return current != null && current.eventId != null
                    ? EventHelper.getEvent(current.eventId) : AbstractDungeon.generateEvent(rng);
        } finally {
            scope--;
        }
    }

    public static void rememberEvent(String id) {
        if (scope > 0 && current != null && current.eventId == null) current.eventId = id;
    }

    public static void updateEvent(AbstractEvent event, boolean enter) {
        boolean record = current != null && AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT
                && !AbstractDungeon.getCurrRoom().isBattleOver;
        if (record) scope++;
        try {
            if (enter) event.onEnterRoom(); else event.update();
        } finally {
            if (record) scope--;
        }
    }

    private static String callKey(String operation) {
        if (scope <= 0 || current == null) return null;
        StringBuilder key = new StringBuilder(operation);
        // Call-site keys isolate branches and nested generators from one another.
        for (StackTraceElement frame : Thread.currentThread().getStackTrace()) {
            String name = frame.getClassName();
            if (name.equals(LordvesselHistory.class.getName()) || name.startsWith("java.")
                    || name.startsWith("patches.Lordvessel")
                    || name.equals(Random.class.getName())) continue;
            key.append('|').append(name).append('.').append(frame.getMethodName());
            if (name.startsWith("com.megacrit.cardcrawl.events.") || name.startsWith("events.")
                    || name.startsWith("com.megacrit.cardcrawl.rooms.")) break;
        }
        return key.toString();
    }

    public static String randomKey(String method) { return callKey("rng:" + method); }

    public static String randomValue(String key) {
        if (key == null) return null;
        ArrayList<String> values = current.random.get(key);
        int index = positions.getOrDefault(key, 0);
        if (values == null || index >= values.size()) return null;
        positions.put(key, index + 1);
        return values.get(index);
    }

    public static void rememberRandom(String key, String value) {
        if (key == null) return;
        current.random.computeIfAbsent(key, ignored -> new ArrayList<>()).add(value);
        positions.put(key, positions.getOrDefault(key, 0) + 1);
    }

    public static String cardsKey(String method) { return callKey("cards:" + method); }

    public static ArrayList<AbstractCard> savedCards(String key) {
        if (key == null) return null;
        ArrayList<ArrayList<CardData>> choices = current.cards.get(key);
        int index = positions.getOrDefault(key, 0);
        if (choices == null || index >= choices.size()) return null;
        ArrayList<AbstractCard> result = new ArrayList<>();
        for (CardData card : choices.get(index)) result.add(card.create());
        positions.put(key, index + 1);
        return result;
    }

    public static void rememberCards(String key, ArrayList<AbstractCard> cards) {
        if (key == null || cards == null) return;
        ArrayList<CardData> result = new ArrayList<>();
        for (AbstractCard card : cards) result.add(new CardData(card));
        current.cards.computeIfAbsent(key, ignored -> new ArrayList<>()).add(result);
        positions.put(key, positions.getOrDefault(key, 0) + 1);
    }

    public static String contentKey(String method) { return callKey("content:" + method); }

    public static Object savedContent(String key, String type) {
        String saved = randomValue(key);
        if (saved == null) return null;
        if (type.equals("card")) return JSON.fromJson(saved, CardData.class).create();
        if (type.equals("relic")) return RelicLibrary.getRelic(saved).makeCopy();
        return PotionHelper.getPotion(saved);
    }

    public static void rememberContent(String key, Object item) {
        if (key == null || item == null) return;
        String value = item instanceof AbstractCard ? JSON.toJson(new CardData((AbstractCard) item))
                : item instanceof AbstractRelic ? ((AbstractRelic) item).relicId : ((AbstractPotion) item).ID;
        rememberRandom(key, value);
    }

    public static void beginChest() {
        // Store outside the instrumented method: its finally handler also covers entry.
        chestScopes.push(scope);
        if (current != null && AbstractDungeon.getCurrRoom() instanceof TreasureRoom) scope++;
    }

    public static void endChest() {
        if (!chestScopes.isEmpty()) scope = chestScopes.pop();
    }

    @Override
    public State onSave() { return state; }

    @Override
    public void onLoad(State saved) {
        reset();
        if (saved != null && saved.rooms != null) state = saved;
    }
}
