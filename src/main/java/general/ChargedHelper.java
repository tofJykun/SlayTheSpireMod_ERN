package general;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;

import java.util.regex.Pattern;

public class ChargedHelper {
    public static final int FULL_HAND_SIZE = 10;
    private static final Pattern CHARGED_KEYWORD = Pattern.compile(
            "(?<![\\p{L}\\p{N}_])(?:充能|charged)(?![\\p{L}\\p{N}_])", Pattern.CASE_INSENSITIVE);

    private ChargedHelper() {
    }

    public static boolean isChargedCard(AbstractCard card) {
        if (card == null) {
            return false;
        }
        if (containsChargedText(card.rawDescription)) {
            return true;
        }
        if (card.keywords != null) {
            for (String keyword : card.keywords) {
                if (containsChargedText(keyword)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isHandFull(AbstractPlayer player) {
        return player != null && player.hand != null && player.hand.size() >= FULL_HAND_SIZE;
    }

    public static String cantUseMessage() {
        return Settings.language == Settings.GameLanguage.ZHS
                ? "手牌未抽满。"
                : "Your hand is not full.";
    }

    private static boolean containsChargedText(String text) {
        if (text == null) {
            return false;
        }
        // Match the keyword, not words such as 充能球 (Orb) or recharged.
        String normalized = text.replaceAll("#[bgryp]", "");
        return CHARGED_KEYWORD.matcher(normalized).find();
    }
}
