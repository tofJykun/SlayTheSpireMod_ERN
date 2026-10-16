package general;

import basemod.BaseMod;
import com.megacrit.cardcrawl.core.Settings;
import java.util.Locale;

public enum CardElement {
    ElementWood("木", "Wood"), ElementFire("火", "Fire"), ElementEarth("土", "Earth"),
    ElementMetal("金", "Metal"), ElementWater("水", "Water");

    public final String chinese;
    public final String english;

    CardElement(String chinese, String english) {
        this.chinese = "元素：" + chinese;
        this.english = "Element: " + english;
    }

    public CardElement forwardPredecessor() { return values()[(ordinal() + 4) % 5]; }
    public CardElement reversePredecessor() { return values()[(ordinal() + 3) % 5]; }

    public static boolean isChineseElementKeyword(String word) {
        for (CardElement element : values()) {
            if (element.chinese.equals(word)) return true;
        }
        return false;
    }

    public static String descriptionTokens(String description) {
        if (description == null) return null;
        for (CardElement value : values()) {
            description = description.replace(value.english, value.english.replace(' ', '_'));
        }
        return description;
    }

    public static void registerKeywords() {
        boolean zh = Settings.language == Settings.GameLanguage.ZHS;
        for (CardElement element : values()) {
            String name = zh ? element.chinese : element.english;
            String description = zh
                    ? "如果打出的上一张牌是 #y" + element.forwardPredecessor().chinese
                        + " ，这张牌会打出两次。 NL 如果打出的上一张牌是 #y"
                        + element.reversePredecessor().chinese + " ，这张牌的耗能增加 #b1 。"
                    : "If the last card played was #yElement: #y" + element.forwardPredecessor().english.substring(9)
                        + ", play this card twice. NL If the last card played was #yElement: #y"
                        + element.reversePredecessor().english.substring(9) + ", this card costs #b1 more Energy.";
            BaseMod.addKeyword(name, new String[] {name.toLowerCase(Locale.ROOT)}, description);
        }
    }
}
