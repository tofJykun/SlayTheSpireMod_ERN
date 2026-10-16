package general;

import cards.scholar.BrandNewWorld;
import com.megacrit.cardcrawl.cards.AbstractCard;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ExclusiveCardPool {
    private ExclusiveCardPool() {}

    public static boolean isExcluded(AbstractCard card) {
        return card != null && BrandNewWorld.ID.equals(card.cardID);
    }

    public static HashMap<String, AbstractCard> candidates(HashMap<String, AbstractCard> library) {
        if (!library.containsKey(BrandNewWorld.ID)) return library;
        // Keep the original iteration order, without removing the card from the library/save lookup.
        HashMap<String, AbstractCard> result = new LinkedHashMap<>();
        for (Map.Entry<String, AbstractCard> entry : library.entrySet()) {
            if (!isExcluded(entry.getValue())) result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
