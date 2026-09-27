package general;

import cards.tempcards.CraftmanCreation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import basemod.abstracts.CustomCard;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

/** The body owns its behavior/state; only its creation may enter a real pile or play queue. */
public final class SmithingBody {
    private static final Map<AbstractCard, WeakReference<CraftmanCreation>> OWNERS = new WeakHashMap<>();
    private static final Map<AbstractCard, CraftmanCreation> COPIES = new WeakHashMap<>();
    private static final Map<AbstractGameAction, CraftmanCreation> ACTIONS = new WeakHashMap<>();
    private static CraftmanCreation active;
    private static int internalCopies;

    private SmithingBody() {}

    public static void bind(AbstractCard body, CraftmanCreation owner) {
        if (body != null) OWNERS.put(body, new WeakReference<>(owner));
    }

    public static CraftmanCreation owner(AbstractCard body) {
        WeakReference<CraftmanCreation> reference = OWNERS.get(body);
        return reference == null ? null : reference.get();
    }

    public static AbstractCard behavior(AbstractCard card) {
        return card instanceof CraftmanCreation && ((CraftmanCreation)card).getLiveBody() != null
                ? ((CraftmanCreation)card).getLiveBody() : card;
    }

    public static AbstractCard physical(AbstractCard card) {
        if (card == null) return null;
        CraftmanCreation owner = owner(card);
        if (owner != null) return owner;
        CraftmanCreation template = COPIES.remove(card);
        if (template == null) return card;
        CraftmanCreation copy = template.copyWithBodyState(card);
        copy.freeToPlayOnce = card.freeToPlayOnce;
        copy.purgeOnUse = card.purgeOnUse;
        copy.isInAutoplay = card.isInAutoplay;
        copy.energyOnUse = card.energyOnUse;
        copy.current_x = card.current_x;
        copy.current_y = card.current_y;
        copy.target_x = card.target_x;
        copy.target_y = card.target_y;
        bind(card, copy);
        return copy;
    }

    public static AbstractCard forQueue(AbstractCard card) {
        AbstractCard result = physical(card);
        if (card != null && result != card) {
            CraftmanCreation creation = (CraftmanCreation)result;
            if (creation.getLiveBody() != card) creation.replaceLiveBodyState(card);
            result.freeToPlayOnce = card.freeToPlayOnce;
            result.purgeOnUse = card.purgeOnUse;
            result.isInAutoplay = card.isInAutoplay;
            result.energyOnUse = card.energyOnUse;
            result.current_x = card.current_x;
            result.current_y = card.current_y;
            result.target_x = card.target_x;
            result.target_y = card.target_y;
        }
        return result;
    }

    public static AbstractCard copy(AbstractCard card) {
        if (card == null) return null;
        internalCopies++;
        try {
            AbstractCard copy = card.makeStatEquivalentCopy();
            if (!(card instanceof CraftmanCreation)) copyFields(card, copy);
            copy.damage = copy.baseDamage;
            copy.block = copy.baseBlock;
            copy.magicNumber = copy.baseMagicNumber;
            copy.isDamageModified = copy.isBlockModified = copy.isMagicNumberModified = false;
            if (patches.ScheduledField.isScheduled(copy)) {
                patches.ScheduledField.setScheduled(copy, patches.ScheduledField.getScheduled(card));
            }
            copy.exhaust = card.exhaust;
            copy.selfRetain = card.selfRetain;
            copy.isEthereal = card.isEthereal;
            copy.isInnate = card.isInnate;
            copy.returnToHand = card.returnToHand;
            copy.shuffleBackIntoDrawPile = card.shuffleBackIntoDrawPile;
            copy.uuid = card.uuid;
            return copy;
        } finally {
            internalCopies--;
        }
    }

    private static void copyFields(AbstractCard source, AbstractCard copy) {
        try {
            for (Class<?> type = source.getClass(); type != AbstractCard.class && type != CustomCard.class;
                    type = type.getSuperclass()) {
                for (Field field : type.getDeclaredFields()) {
                    if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) continue;
                    field.setAccessible(true);
                    Object value = field.get(source);
                    if (value instanceof java.util.List) {
                        ArrayList<Object> values = new ArrayList<>();
                        for (Object item : (java.util.List<?>)value) {
                            values.add(item instanceof AbstractCard ? copy((AbstractCard)item) : item);
                        }
                        if (Modifier.isFinal(field.getModifiers())) replaceList(field.get(copy), values);
                        else field.set(copy, values);
                    } else if (!Modifier.isFinal(field.getModifiers())) {
                        field.set(copy, value instanceof AbstractCard ? copy((AbstractCard)value) : value);
                    }
                }
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot copy smithing body " + source.cardID, e);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void replaceList(Object target, ArrayList<Object> values) {
        java.util.List list = (java.util.List)target;
        list.clear();
        list.addAll(values);
    }

    public static void copied(AbstractCard source, AbstractCard result) {
        if (internalCopies > 0 || source == result || result == null) return;
        CraftmanCreation owner = owner(source);
        if (owner == null) owner = COPIES.get(source);
        if (owner != null) {
            // Freeze materials now; a later DemonTitanite refresh must not rewrite this copy.
            internalCopies++;
            try {
                COPIES.put(result, (CraftmanCreation)owner.makeStatEquivalentCopy());
            } finally {
                internalCopies--;
            }
            result.freeToPlayOnce = owner.freeToPlayOnce;
        }
    }

    public static void run(CraftmanCreation owner, Runnable callback) {
        CraftmanCreation previous = active;
        active = owner;
        try {
            callback.run();
        } finally {
            active = previous;
            owner.commitBodyState();
        }
    }

    public static CraftmanCreation active() {
        return active;
    }

    public static void queued(AbstractGameAction action) {
        if (active != null) ACTIONS.put(action, active);
    }

    public static void updateAction(AbstractGameAction action) {
        CraftmanCreation owner = ACTIONS.get(action);
        if (owner == null) action.update();
        else run(owner, action::update);
        if (action.isDone) ACTIONS.remove(action);
    }

    public static ArrayList<AbstractCard> behaviorCards(CardGroup group) {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        if (group != null) for (AbstractCard card : group.group) cards.add(behavior(card));
        return cards;
    }

    public static HashSet<AbstractCard> instances(UUID uuid) {
        HashSet<AbstractCard> result = new HashSet<>();
        if (AbstractDungeon.player == null || uuid == null) return result;
        addInstance(result, AbstractDungeon.player.cardInUse, uuid);
        for (CardGroup group : new CardGroup[] {AbstractDungeon.player.hand, AbstractDungeon.player.drawPile,
                AbstractDungeon.player.discardPile, AbstractDungeon.player.exhaustPile, AbstractDungeon.player.limbo}) {
            if (group != null) for (AbstractCard card : group.group) addInstance(result, card, uuid);
        }
        if (active != null) addInstance(result, active, uuid);
        return result;
    }

    private static void addInstance(HashSet<AbstractCard> result, AbstractCard card, UUID uuid) {
        if (card == null) return;
        AbstractCard body = behavior(card);
        if (uuid.equals(body.uuid)) result.add(body);
        else if (uuid.equals(card.uuid)) result.add(card);
    }

    public static void clear() {
        OWNERS.clear();
        COPIES.clear();
        ACTIONS.clear();
        active = null;
    }
}
