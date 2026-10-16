package variables;

import cards.revenant.LotOfRunes;
import cards.undertaker.SacredRelicSword;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class LotOfRunesInsightVariable extends UpgradePreviewVariable {
    @Override public String key() { return "MG"; }
    @Override protected boolean isValueModified(AbstractCard card) { return false; }
    @Override protected int currentValue(AbstractCard card) {
        return card instanceof SacredRelicSword ? SacredRelicSword.GOLD_PER_HIT : 6;
    }
    @Override public int baseValue(AbstractCard card) { return currentValue(card); }
    @Override public boolean upgraded(AbstractCard card) { return false; }
}
