package variables;

import cards.tempcards.CrucibleToken;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class CrucibleTokenScarletRotVariable extends UpgradePreviewVariable {
    @Override
    public String key() {
        return "MR";
    }

    @Override
    protected boolean isValueModified(AbstractCard card) {
        return false;
    }

    @Override
    protected int currentValue(AbstractCard card) {
        return card instanceof CrucibleToken ? ((CrucibleToken)card).scarletRot : 0;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return card instanceof CrucibleToken ? ((CrucibleToken)card).baseScarletRot : 0;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return false;
    }
}
