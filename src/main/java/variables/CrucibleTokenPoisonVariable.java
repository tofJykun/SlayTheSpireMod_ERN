package variables;

import basemod.abstracts.DynamicVariable;
import cards.tempcards.CrucibleToken;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class CrucibleTokenPoisonVariable extends DynamicVariable {
    @Override
    public String key() {
        return "MP";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return false;
    }

    @Override
    public int value(AbstractCard card) {
        return card instanceof CrucibleToken ? ((CrucibleToken)card).poison : 0;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return card instanceof CrucibleToken ? ((CrucibleToken)card).basePoison : 0;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return false;
    }
}
