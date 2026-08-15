package variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import patches.ScheduledField;

public class ScheduledVariable extends DynamicVariable {
    @Override
    public String key() {
        return "MS";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ScheduledField.getScheduled(card) != ScheduledField.getBaseScheduled(card);
    }

    @Override
    public int value(AbstractCard card) {
        return ScheduledField.getScheduled(card);
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ScheduledField.getBaseScheduled(card);
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return false;
    }
}
