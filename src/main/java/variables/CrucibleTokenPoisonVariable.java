package variables;

import basemod.abstracts.DynamicVariable;
import cards.scholar.TranquilWalkOfPeace;
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
        if (card instanceof TranquilWalkOfPeace) {
            return ((TranquilWalkOfPeace)card).strength;
        }
        return card instanceof CrucibleToken ? ((CrucibleToken)card).poison : 0;
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof TranquilWalkOfPeace) {
            return ((TranquilWalkOfPeace)card).baseStrength;
        }
        return card instanceof CrucibleToken ? ((CrucibleToken)card).basePoison : 0;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return false;
    }
}
