package general;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import patches.ReplayField;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Keeps combat mutations separate from the reversible upgrades supplied by materials. */
public final class SmithingBodyState {
    private final AbstractCard source;
    private AbstractCard live;
    private final Map<Field, Object> baseline = new LinkedHashMap<>();
    private int replay;

    public SmithingBodyState(AbstractCard source, AbstractCard live) {
        this.source = source;
        this.live = live;
        if (live == null) return;
        try {
            for (String name : Arrays.asList("baseDamage", "baseBlock", "baseMagicNumber", "misc", "cost",
                    "exhaust", "selfRetain", "isEthereal", "isInnate", "isCostModified", "returnToHand", "shuffleBackIntoDrawPile")) {
                Field field = AbstractCard.class.getField(name);
                baseline.put(field, snapshot(field.get(live)));
            }
            for (Class<?> type = live.getClass(); type != AbstractCard.class && type != CustomCard.class;
                    type = type.getSuperclass()) {
                for (Field field : type.getDeclaredFields()) {
                    if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) continue;
                    field.setAccessible(true);
                    baseline.put(field, snapshot(field.get(live)));
                }
            }
            replay = ReplayField.getReplay(live);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot capture smithing body " + live.cardID, e);
        }
    }

    public AbstractCard card() {
        return live;
    }

    public void replace(AbstractCard replacement) {
        this.live = replacement;
        commit();
    }

    public void commit() {
        if (live == null) return;
        try {
            for (Map.Entry<Field, Object> entry : baseline.entrySet()) {
                Field field = entry.getKey();
                Object before = entry.getValue();
                Object after = field.get(live);
                if (!Objects.equals(before, after)) {
                    // A +3 damage material upgrade is not a permanent +3 to the original body.
                    if (field.getType() == int.class) {
                        field.setInt(source, field.getInt(source) + (Integer)after - (Integer)before);
                    } else if (after instanceof List && field.get(source) instanceof List) {
                        replaceList(field.get(source), after);
                    } else if (!Modifier.isFinal(field.getModifiers())) {
                        field.set(source, snapshot(after));
                    }
                    entry.setValue(snapshot(after));
                }
            }
            int currentReplay = ReplayField.getReplay(live);
            ReplayField.setReplay(source, ReplayField.getReplay(source) + currentReplay - replay);
            replay = currentReplay;
            source.magicNumber = source.baseMagicNumber;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot save smithing body " + live.cardID, e);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void replaceList(Object target, Object value) {
        List list = (List)target;
        list.clear();
        list.addAll((List)value);
    }

    private static Object snapshot(Object value) {
        return value instanceof List ? new ArrayList<>((List<?>)value) : value;
    }
}
