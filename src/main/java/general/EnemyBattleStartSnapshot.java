package general;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnemyBattleStartSnapshot {
    private static final Object UNSUPPORTED = new Object();
    private static final ArrayList<MonsterStateSnapshot> MONSTER_SNAPSHOTS = new ArrayList<>();

    private static boolean captured = false;
    private static String encounterKey;
    private static Random generationMiscRng;
    private static Random generationAiRng;

    private EnemyBattleStartSnapshot() {}

    public static void prepareForRoomGeneration() {
        reset();
        generationMiscRng = AbstractDungeon.miscRng == null ? null : AbstractDungeon.miscRng.copy();
        generationAiRng = AbstractDungeon.aiRng == null ? null : AbstractDungeon.aiRng.copy();
    }

    public static void captureIfNeeded() {
        if (captured || AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getCurrRoom().monsters == null) {
            return;
        }
        capture();
    }

    public static void capture() {
        MONSTER_SNAPSHOTS.clear();
        if (AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getCurrRoom().monsters == null) {
            captured = false;
            return;
        }

        encounterKey = AbstractDungeon.lastCombatMetricKey;
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            MONSTER_SNAPSHOTS.add(new MonsterStateSnapshot(monster));
        }
        captured = encounterKey != null && !encounterKey.isEmpty() && !MONSTER_SNAPSHOTS.isEmpty();
    }

    public static void restore() {
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if (!captured || room == null || room.monsters == null || encounterKey == null) {
            return;
        }

        MonsterGroup freshGroup = createFreshInitialGroup();
        if (freshGroup == null || freshGroup.monsters.isEmpty()) {
            return;
        }

        int count = Math.min(MONSTER_SNAPSHOTS.size(), freshGroup.monsters.size());
        for (int i = 0; i < count; i++) {
            MONSTER_SNAPSHOTS.get(i).restoreTo(freshGroup.monsters.get(i));
        }
        room.monsters = freshGroup;
        room.monsters.showIntent();
        if (room.phase == AbstractRoom.RoomPhase.COMPLETE) {
            room.phase = AbstractRoom.RoomPhase.COMBAT;
        }
    }

    public static void reset() {
        MONSTER_SNAPSHOTS.clear();
        captured = false;
        encounterKey = null;
        generationMiscRng = null;
        generationAiRng = null;
    }

    private static MonsterGroup createFreshInitialGroup() {
        Random currentMiscRng = AbstractDungeon.miscRng == null ? null : AbstractDungeon.miscRng.copy();
        Random currentAiRng = AbstractDungeon.aiRng == null ? null : AbstractDungeon.aiRng.copy();
        try {
            if (generationMiscRng != null) {
                AbstractDungeon.miscRng = generationMiscRng.copy();
            }
            if (generationAiRng != null) {
                AbstractDungeon.aiRng = generationAiRng.copy();
            }
            MonsterGroup freshGroup = MonsterHelper.getEncounter(encounterKey);
            if (freshGroup != null) {
                freshGroup.init();
            }
            return freshGroup;
        } finally {
            if (currentMiscRng != null) {
                AbstractDungeon.miscRng = currentMiscRng;
            }
            if (currentAiRng != null) {
                AbstractDungeon.aiRng = currentAiRng;
            }
        }
    }

    private static class MonsterStateSnapshot {
        private final int maxHealth;
        private final int currentHealth;
        private final int currentBlock;
        private final ArrayList<PowerSnapshot> powers;

        private MonsterStateSnapshot(AbstractMonster monster) {
            this.maxHealth = monster.maxHealth;
            this.currentHealth = monster.currentHealth;
            this.currentBlock = monster.currentBlock;
            this.powers = new ArrayList<>();
            for (AbstractPower power : monster.powers) {
                this.powers.add(new PowerSnapshot(power, monster));
            }
        }

        private void restoreTo(AbstractMonster monster) {
            monster.maxHealth = this.maxHealth;
            monster.currentHealth = Math.min(this.currentHealth, monster.maxHealth);
            monster.currentBlock = this.currentBlock;
            monster.powers.clear();
            for (PowerSnapshot powerSnapshot : this.powers) {
                monster.powers.add(powerSnapshot.restore(monster));
            }
            monster.healthBarRevivedEvent();
            monster.healthBarUpdatedEvent();
            monster.showHealthBar();
            monster.applyPowers();
        }
    }

    private static class PowerSnapshot {
        private final AbstractPower power;
        private final ArrayList<FieldSnapshot> fields;

        private PowerSnapshot(AbstractPower power, AbstractMonster owner) {
            this.power = power;
            this.fields = snapshotFields(power, owner);
        }

        private AbstractPower restore(AbstractMonster owner) {
            restoreFields(this.fields, this.power, owner);
            this.power.owner = owner;
            this.power.updateDescription();
            return this.power;
        }
    }

    private static class FieldSnapshot {
        private final Field field;
        private final Object value;

        private FieldSnapshot(Field field, Object value) {
            this.field = field;
            this.value = value;
        }
    }

    private static ArrayList<FieldSnapshot> snapshotFields(Object source, AbstractMonster owner) {
        ArrayList<FieldSnapshot> snapshots = new ArrayList<>();
        Class<?> type = source.getClass();
        while (type != null && type != Object.class) {
            for (Field field : type.getDeclaredFields()) {
                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) {
                    continue;
                }
                try {
                    field.setAccessible(true);
                    Object copied = copyValue(field.get(source), owner);
                    if (copied != UNSUPPORTED) {
                        snapshots.add(new FieldSnapshot(field, copied));
                    }
                } catch (Exception ignored) {
                }
            }
            type = type.getSuperclass();
        }
        return snapshots;
    }

    private static void restoreFields(ArrayList<FieldSnapshot> fields, Object target, AbstractMonster owner) {
        for (FieldSnapshot snapshot : fields) {
            try {
                snapshot.field.setAccessible(true);
                snapshot.field.set(target, copyValue(snapshot.value, owner));
            } catch (Exception ignored) {
            }
        }
    }

    private static Object copyValue(Object value, AbstractMonster owner) {
        if (value == null) {
            return null;
        }
        if (value instanceof String || value instanceof Number || value instanceof Boolean || value instanceof Character
                || value.getClass().isEnum()) {
            return value;
        }
        if (value instanceof Color) {
            return ((Color)value).cpy();
        }
        if (value instanceof EnemyMoveInfo) {
            EnemyMoveInfo move = (EnemyMoveInfo)value;
            return new EnemyMoveInfo(move.nextMove, move.intent, move.baseDamage, move.multiplier, move.isMultiDamage);
        }
        if (value instanceof DamageInfo) {
            DamageInfo info = (DamageInfo)value;
            AbstractCreature infoOwner = info.owner == null ? owner : info.owner;
            DamageInfo copy = new DamageInfo(infoOwner, info.base, info.type);
            copy.output = info.output;
            copy.isModified = info.isModified;
            copy.name = info.name;
            return copy;
        }
        if (value instanceof ArrayList) {
            ArrayList<?> list = (ArrayList<?>)value;
            ArrayList<Object> copy = new ArrayList<>();
            for (Object item : list) {
                Object copied = copyValue(item, owner);
                if (copied == UNSUPPORTED) {
                    return UNSUPPORTED;
                }
                copy.add(copied);
            }
            return copy;
        }
        if (value instanceof HashMap) {
            HashMap<?, ?> map = (HashMap<?, ?>)value;
            HashMap<Object, Object> copy = new HashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                Object key = copyValue(entry.getKey(), owner);
                Object copiedValue = copyValue(entry.getValue(), owner);
                if (key == UNSUPPORTED || copiedValue == UNSUPPORTED) {
                    return UNSUPPORTED;
                }
                copy.put(key, copiedValue);
            }
            return copy;
        }
        return UNSUPPORTED;
    }
}
