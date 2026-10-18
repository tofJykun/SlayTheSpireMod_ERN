package general;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/** Save gameplay fields, not live owners, GL objects or queued visual effects. */
public final class CarriedPowerSnapshot {
    public String className;
    public String fields;
    public String imagePath;
    public String region48;
    public String region128;

    private static final Gson JSON = new GsonBuilder()
            .setExclusionStrategies(new ExclusionStrategy() {
                public boolean shouldSkipClass(Class<?> type) {
                    return Texture.class.isAssignableFrom(type) || TextureRegion.class.isAssignableFrom(type)
                            || AbstractGameAction.class.isAssignableFrom(type);
                }
                public boolean shouldSkipField(FieldAttributes field) {
                    return (field.getDeclaringClass() == AbstractPower.class && "effect".equals(field.getName()))
                            || "general.GreyHealthAttackReduction".equals(field.getDeclaredClass().getName())
                            || ("powers.RingedKnightStraightSwordPower".equals(field.getDeclaringClass().getName())
                                && "pendingEnergy".equals(field.getName()));
                }
            })
            .registerTypeHierarchyAdapter(AbstractCreature.class, new CreatureAdapter())
            .registerTypeHierarchyAdapter(AbstractRoom.class, new RoomAdapter())
            .registerTypeHierarchyAdapter(AbstractCard.class, new CardAdapter())
            .registerTypeAdapterFactory(new PowerTypes())
            .enableComplexMapKeySerialization().create();

    public static CarriedPowerSnapshot capture(AbstractPower power) {
        CarriedPowerSnapshot snapshot = new CarriedPowerSnapshot();
        snapshot.className = power.getClass().getName();
        snapshot.fields = JSON.toJson(power);
        if (power.img != null && power.img.getTextureData() instanceof FileTextureData) {
            snapshot.imagePath = ((FileTextureData)power.img.getTextureData()).getFileHandle().path();
        }
        snapshot.region48 = power.region48 == null ? null : power.region48.name;
        snapshot.region128 = power.region128 == null ? null : power.region128.name;
        return snapshot;
    }

    public AbstractPower restore() {
        try {
            Class<? extends AbstractPower> type = Class.forName(className).asSubclass(AbstractPower.class);
            // Gson restores private/final counters without running constructors with gameplay side effects.
            AbstractPower power = JSON.fromJson(fields, type);
            power.owner = AbstractDungeon.player;
            ReflectionHacks.setPrivate(power, AbstractPower.class, "effect", new ArrayList<>());
            power.img = imagePath == null ? null : ImageMaster.loadImage(imagePath);
            if (region48 != null) power.region48 = AbstractPower.atlas.findRegion(region48);
            if (region128 != null) power.region128 = AbstractPower.atlas.findRegion(region128);
            if (power.img == null && power.region48 == null && power.region128 == null) {
                // Hidden powers do not render this; unknown external textures still get a safe icon.
                power.region48 = AbstractPower.atlas.findRegion("48/strength");
                power.region128 = AbstractPower.atlas.findRegion("128/strength");
            }
            reserveUniqueId(type, power.ID);
            // Recreate non-persistent helpers whose back-reference would serialize the power recursively.
            for (Field field : type.getDeclaredFields()) {
                if ("general.GreyHealthAttackReduction".equals(field.getType().getName())) {
                    field.setAccessible(true);
                    field.set(power, field.getType().getConstructor(AbstractPower.class).newInstance(power));
                }
            }
            if ("powers.RingedKnightStraightSwordPower".equals(className)) {
                Field field = type.getDeclaredField("pendingEnergy");
                field.setAccessible(true);
                field.set(power, new java.util.IdentityHashMap<>());
            }
            if ("powers.ElementalDefensePower".equals(className)) {
                Field field = type.getDeclaredField("reducedCardUuids");
                field.setAccessible(true);
                ((java.util.Set<?>)field.get(power)).clear();
            }
            if ("powers.EarthshakerPower".equals(className)) {
                Field field = type.getDeclaredField("damage");
                field.setAccessible(true);
                int[] old = (int[])field.get(power);
                int[] damage = java.util.Arrays.copyOf(old, AbstractDungeon.getMonsters().monsters.size());
                for (int i = old.length; i < damage.length; i++) damage[i] = old.length == 0 ? 0 : old[0];
                field.set(power, damage);
            }
            if ("powers.BorrowedTimePower".equals(className)
                    || "powers.BedOfMagicCostReductionPower".equals(className)) {
                for (String key : new String[] {"originalCosts", "originalModifiedFlags"}) {
                    Field field = type.getDeclaredField(key);
                    field.setAccessible(true);
                    ((Map<?, ?>)field.get(power)).clear();
                }
            }
            power.updateDescription();
            return power;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot restore carried power " + className, e);
        }
    }

    private static final class RoomAdapter implements JsonSerializer<AbstractRoom>, JsonDeserializer<AbstractRoom> {
        public JsonElement serialize(AbstractRoom room, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(true);
        }
        public AbstractRoom deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
            return AbstractDungeon.getCurrRoom();
        }
    }

    private static void reserveUniqueId(Class<?> type, String id) throws IllegalAccessException {
        try {
            Field base = type.getDeclaredField("POWER_ID");
            Field next;
            try { next = type.getDeclaredField("nextId"); }
            catch (NoSuchFieldException e) { next = type.getDeclaredField("powerIdOffset"); }
            base.setAccessible(true);
            next.setAccessible(true);
            String prefix = (String)base.get(null);
            if (Modifier.isStatic(next.getModifiers()) && id.startsWith(prefix)) {
                long value = Long.parseLong(id.substring(prefix.length()));
                if (next.getType() == long.class) next.setLong(null, Math.max(next.getLong(null), value + 1));
                else if (next.getType() == int.class) next.setInt(null, Math.max(next.getInt(null), (int)value + 1));
            }
        } catch (NoSuchFieldException | NumberFormatException ignored) {
            // Only independently numbered powers have this pair of fields.
        }
    }

    /** Nested powers (e.g. Rallying Standard's summon template) also need their concrete class. */
    private static final class PowerTypes implements TypeAdapterFactory {
        @Override @SuppressWarnings({"unchecked", "rawtypes"})
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> token) {
            if (!AbstractPower.class.isAssignableFrom(token.getRawType())) return null;
            return new TypeAdapter<T>() {
                @Override public void write(JsonWriter out, T value) throws IOException {
                    if (value == null) { out.nullValue(); return; }
                    JsonObject data = new JsonObject();
                    data.addProperty("class", value.getClass().getName());
                    TypeAdapter delegate = gson.getDelegateAdapter(PowerTypes.this, TypeToken.get(value.getClass()));
                    data.add("data", delegate.toJsonTree(value));
                    gson.getAdapter(JsonElement.class).write(out, data);
                }
                @Override public T read(JsonReader in) throws IOException {
                    JsonElement element = gson.getAdapter(JsonElement.class).read(in);
                    if (element.isJsonNull()) return null;
                    JsonObject data = element.getAsJsonObject();
                    try {
                        Class<?> actual = Class.forName(data.get("class").getAsString());
                        if (!token.getRawType().isAssignableFrom(actual)) throw new JsonParseException("Invalid power type");
                        return (T)gson.getDelegateAdapter(PowerTypes.this, TypeToken.get(actual)).fromJsonTree(data.get("data"));
                    } catch (ClassNotFoundException e) { throw new JsonParseException(e); }
                }
            };
        }
    }

    private static final class CreatureAdapter implements JsonSerializer<AbstractCreature>, JsonDeserializer<AbstractCreature> {
        public JsonElement serialize(AbstractCreature creature, Type type, JsonSerializationContext context) {
            JsonObject data = new JsonObject();
            data.addProperty("player", creature.isPlayer);
            data.addProperty("name", creature.name);
            return data;
        }
        public AbstractCreature deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
            JsonObject data = json.getAsJsonObject();
            return data.get("player").getAsBoolean() ? AbstractDungeon.player
                    : new DepartedEnemy(data.get("name").getAsString());
        }
    }

    // An effect aimed at a previous enemy must not silently switch to an enemy in the next fight.
    private static final class DepartedEnemy extends AbstractMonster {
        DepartedEnemy(String name) {
            super(name, "ERN:DepartedEnemy", 1, 0, 0, 1, 1, null);
            currentHealth = 0;
            isDead = true;
        }
        @Override public void takeTurn() { }
        @Override protected void getMove(int roll) { }
    }

    private static final class CardAdapter implements JsonSerializer<AbstractCard>, JsonDeserializer<AbstractCard> {
        public JsonElement serialize(AbstractCard card, Type type, JsonSerializationContext context) {
            JsonObject data = new JsonObject();
            data.addProperty("id", card.cardID);
            data.addProperty("upgrades", card.timesUpgraded);
            data.addProperty("misc", card.misc);
            data.addProperty("uuid", card.uuid.toString());
            data.addProperty("sown", patches.SowingField.isSown(card));
            Map<String, String> values = new LinkedHashMap<>();
            for (Field field : AbstractCard.class.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) continue;
                if (!field.getType().isPrimitive()) continue;
                try { field.setAccessible(true); values.put(field.getName(), JSON.toJson(field.get(card))); }
                catch (IllegalAccessException e) { throw new JsonParseException(e); }
            }
            data.add("values", context.serialize(values));
            return data;
        }
        public AbstractCard deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
            JsonObject data = json.getAsJsonObject();
            AbstractCard card = CardLibrary.getCopy(data.get("id").getAsString(),
                    data.get("upgrades").getAsInt(), data.get("misc").getAsInt());
            card.uuid = UUID.fromString(data.get("uuid").getAsString());
            for (Map.Entry<String, JsonElement> entry : data.getAsJsonObject("values").entrySet()) {
                try {
                    Field field = AbstractCard.class.getDeclaredField(entry.getKey());
                    field.setAccessible(true);
                    field.set(card, JSON.fromJson(entry.getValue().getAsString(), field.getType()));
                } catch (ReflectiveOperationException e) { throw new JsonParseException(e); }
            }
            if (data.get("sown").getAsBoolean()) patches.SowingField.sow(card);
            card.initializeDescription();
            return card;
        }
    }
}
