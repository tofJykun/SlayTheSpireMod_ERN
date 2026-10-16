package variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/** Custom numbers follow the same displayUpgrades/reset lifecycle as D, B and M. */
public abstract class UpgradePreviewVariable extends DynamicVariable {
    private static final Map<AbstractCard, Set<String>> previews = new WeakHashMap<>();

    @Override
    public final void setIsModified(AbstractCard card, boolean modified) {
        if (modified) previews.computeIfAbsent(card, c -> new HashSet<>()).add(key());
        else if (previews.containsKey(card)) previews.get(card).remove(key());
    }

    private boolean isUpgradePreview(AbstractCard card) {
        return previews.containsKey(card) && previews.get(card).contains(key());
    }

    public static void reset(AbstractCard card) { previews.remove(card); }

    @Override
    public final boolean isModified(AbstractCard card) {
        return isUpgradePreview(card) || isValueModified(card);
    }

    @Override
    public final int value(AbstractCard card) {
        // A smaller upgraded number (e.g. Scheduled 3 -> 2) is green too.
        return isUpgradePreview(card) ? modifiedBaseValue(card) : currentValue(card);
    }

    protected abstract boolean isValueModified(AbstractCard card);
    protected abstract int currentValue(AbstractCard card);
}
