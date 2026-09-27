package variables;

import basemod.abstracts.DynamicVariable;
import cards.executor.DarkBlood;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class DarkBloodAberrationVariable extends DynamicVariable {
    @Override
    public String key() {
        return "MB";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return false;
    }

    @Override
    public int value(AbstractCard card) {
        return card instanceof DarkBlood ? DarkBlood.ABERRATION : 0;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return value(card);
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return false;
    }
}
