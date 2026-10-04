package general;

import basemod.BaseMod;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import powers.SpiritPower;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class SummonKeywordHelper {
    private static final Map<String, SummonKeywordHelper> KEYWORDS = new HashMap<>();
    private final int baseBlock;
    private final String template;

    private SummonKeywordHelper(int baseBlock, String template) {
        this.baseBlock = baseBlock;
        this.template = template;
    }

    public static void register(String name, String[] aliases, int baseBlock, String template) {
        SummonKeywordHelper keyword = new SummonKeywordHelper(baseBlock, template);
        // Keyword registration precedes FontHelper initialization; do not initialize AbstractDungeon here.
        BaseMod.addKeyword(name, aliases, String.format(Locale.ROOT, template, baseBlock));
        for (String alias : aliases) {
            KEYWORDS.put(alias.toLowerCase(Locale.ROOT), keyword);
        }
    }

    private String description() {
        return String.format(Locale.ROOT, template, SpiritPower.summonBlock(AbstractDungeon.player, baseBlock));
    }

    public static void refreshDictionary() {
        for (Map.Entry<String, SummonKeywordHelper> entry : KEYWORDS.entrySet()) {
            GameDictionary.keywords.put(entry.getKey(), entry.getValue().description());
        }
    }

    public static void refreshTip(PowerTip tip) {
        if (tip.header == null) {
            return;
        }
        SummonKeywordHelper keyword = KEYWORDS.get(tip.header.toLowerCase(Locale.ROOT));
        if (keyword != null) {
            tip.body = keyword.description();
        }
    }
}
