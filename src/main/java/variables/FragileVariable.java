package variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import patches.FragileField;

public class FragileVariable extends DynamicVariable {
    @Override public String key() { return "ERNFragile"; }
    @Override public int value(AbstractCard card) { return FragileField.value(card); }
    @Override public int baseValue(AbstractCard card) {
        FragileField.State s = FragileField.state(card);
        return s == null ? 0 : s.base;
    }
    @Override public boolean isModified(AbstractCard card) { return value(card) != baseValue(card); }
    @Override public boolean upgraded(AbstractCard card) {
        FragileField.State s = FragileField.state(card);
        return s != null && s.upgraded;
    }
}
