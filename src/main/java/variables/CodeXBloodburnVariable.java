package variables;

import basemod.abstracts.DynamicVariable;
import cards.tempcards.CodeX;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class CodeXBloodburnVariable extends DynamicVariable {
    @Override
    public String key() {
        return "MD";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return false;
    }

    @Override
    public int value(AbstractCard card) {
        return card instanceof CodeX ? ((CodeX)card).bloodburn : 0;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return card instanceof CodeX ? ((CodeX)card).baseBloodburn : 0;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return false;
    }
}
