package variables;

import basemod.abstracts.DynamicVariable;
import cards.tempcards.CodeX;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class CodeXScarletRotVariable extends DynamicVariable {
    @Override
    public String key() {
        return "MR";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return false;
    }

    @Override
    public int value(AbstractCard card) {
        return card instanceof CodeX ? ((CodeX)card).scarletRot : 0;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return card instanceof CodeX ? ((CodeX)card).baseScarletRot : 0;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return false;
    }
}
