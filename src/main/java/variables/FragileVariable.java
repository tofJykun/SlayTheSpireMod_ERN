package variables;

import com.megacrit.cardcrawl.cards.AbstractCard;
import patches.FragileField;

public class FragileVariable extends UpgradePreviewVariable {
    @Override public String key() { return "ERNFragile"; }
    @Override protected int currentValue(AbstractCard card) { return FragileField.value(card); }
    @Override public int baseValue(AbstractCard card) {
        FragileField.State s = FragileField.state(card);
        return s == null ? 0 : s.base;
    }
    @Override protected boolean isValueModified(AbstractCard card) { return value(card) != baseValue(card); }
    @Override public boolean upgraded(AbstractCard card) {
        FragileField.State s = FragileField.state(card);
        return s != null && s.upgraded;
    }
}
