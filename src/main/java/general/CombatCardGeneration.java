package general;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/** Candidate filtering only: never suppress explicit copies or card movement. */
public final class CombatCardGeneration {
    private CombatCardGeneration() {}

    public static boolean isEligible(AbstractCard card) {
        return card != null && !card.hasTag(AbstractCard.CardTags.HEALING)
                && !ExclusiveCardPool.isExcluded(card);
    }

    /** General random/transform APIs also serve events, shops and rewards. */
    public static boolean isCombatActive() {
        AbstractRoom room = CombatState.currentRoom();
        return room != null && CombatState.isInCombat() && !room.isBattleOver;
    }

    public static HashMap<String, AbstractCard> libraryCandidates(HashMap<String, AbstractCard> library) {
        HashMap<String, AbstractCard> available = ExclusiveCardPool.candidates(library);
        if (!isCombatActive()) return available;
        HashMap<String, AbstractCard> result = new LinkedHashMap<>();
        for (Map.Entry<String, AbstractCard> entry : available.entrySet()) {
            if (isEligible(entry.getValue())) result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
