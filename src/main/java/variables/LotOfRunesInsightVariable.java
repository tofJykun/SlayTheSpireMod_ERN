package variables;

import basemod.abstracts.DynamicVariable;
import cards.revenant.LotOfRunes;
import cards.undertaker.SacredRelicSword;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class LotOfRunesInsightVariable extends DynamicVariable {
    @Override public String key() { return "MG"; }
    @Override public boolean isModified(AbstractCard card) { return card instanceof LotOfRunes; }
    @Override public int value(AbstractCard card) {
        return card instanceof SacredRelicSword ? SacredRelicSword.GOLD_PER_HIT : 6;
    }
    @Override public int baseValue(AbstractCard card) { return value(card); }
    @Override public boolean upgraded(AbstractCard card) { return false; }
}
