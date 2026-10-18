package general;

import cards.scholar.BrandNewWorld;
import cards.scholar.Alkahest;
import cards.scholar.LordOfLords;
import cards.scholar.ParadiseLost;
import cards.scholar.WhiteNight;
import cards.scholar.Emerge;
import cards.scholar.Daat;
import cards.scholar.TreeOfLife;
import cards.scholar.Empyrean;
import com.megacrit.cardcrawl.cards.AbstractCard;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ExclusiveCardPool {
    private ExclusiveCardPool() {}

    public static boolean isExcluded(AbstractCard card) {
        return card != null && (BrandNewWorld.ID.equals(card.cardID) || Alkahest.ID.equals(card.cardID)
                || LordOfLords.ID.equals(card.cardID) || ParadiseLost.ID.equals(card.cardID) || Emerge.ID.equals(card.cardID)
                || Daat.ID.equals(card.cardID)
                || TreeOfLife.ID.equals(card.cardID)
                || Empyrean.ID.equals(card.cardID)
                || (WhiteNight.ID.equals(card.cardID) && WhiteNightProgress.isUnavailable()));
    }

    public static HashMap<String, AbstractCard> candidates(HashMap<String, AbstractCard> library) {
        if (!library.containsKey(BrandNewWorld.ID) && !library.containsKey(Alkahest.ID)
                && !library.containsKey(LordOfLords.ID) && !library.containsKey(ParadiseLost.ID)
                && !library.containsKey(WhiteNight.ID) && !library.containsKey(Emerge.ID)
                && !library.containsKey(Daat.ID) && !library.containsKey(TreeOfLife.ID)
                && !library.containsKey(Empyrean.ID)) return library;
        // Keep the original iteration order, without removing the card from the library/save lookup.
        HashMap<String, AbstractCard> result = new LinkedHashMap<>();
        for (Map.Entry<String, AbstractCard> entry : library.entrySet()) {
            if (!isExcluded(entry.getValue())) result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
