package general;

import com.megacrit.cardcrawl.cards.AbstractCard;

public class FinalPlanHelper {
    private FinalPlanHelper() {
    }

    public static boolean isFinalPlanCard(AbstractCard card) {
        if (card == null) {
            return false;
        }
        if (containsFinalPlanText(card.rawDescription)) {
            return true;
        }
        if (card.keywords != null) {
            for (String keyword : card.keywords) {
                if (containsFinalPlanText(keyword)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean containsFinalPlanText(String text) {
        if (text == null) {
            return false;
        }
        String normalized = text.toLowerCase();
        return normalized.contains("锦囊")
                || normalized.contains("final plan")
                || normalized.contains("finalplan");
    }
}
