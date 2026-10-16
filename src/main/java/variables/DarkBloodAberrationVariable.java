package variables;

import cards.executor.DarkBlood;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class DarkBloodAberrationVariable extends UpgradePreviewVariable {
    @Override
    public String key() {
        return "MB";
    }

    @Override
    protected boolean isValueModified(AbstractCard card) {
        return false;
    }

    @Override
    protected int currentValue(AbstractCard card) {
        return card instanceof DarkBlood ? DarkBlood.ABERRATION : 0;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return currentValue(card);
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return false;
    }
}
