package variables;

import cards.ironeye.PoisonMarking;
import cards.scholar.TranquilWalkOfPeace;
import cards.tempcards.CrucibleToken;
import cards.undertaker.Blinkbolt;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class CrucibleTokenPoisonVariable extends UpgradePreviewVariable {
    @Override
    public String key() {
        return "MP";
    }

    @Override
    protected boolean isValueModified(AbstractCard card) {
        return false;
    }

    @Override
    protected int currentValue(AbstractCard card) {
        if (card instanceof Blinkbolt) {
            return ((Blinkbolt)card).getDrawPerEnemy();
        }
        if (card instanceof PoisonMarking) {
            return ((PoisonMarking)card).getPoison();
        }
        if (card instanceof TranquilWalkOfPeace) {
            return ((TranquilWalkOfPeace)card).strength;
        }
        return card instanceof CrucibleToken ? ((CrucibleToken)card).poison : 0;
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof Blinkbolt) {
            return ((Blinkbolt)card).getDrawPerEnemy();
        }
        if (card instanceof PoisonMarking) {
            return ((PoisonMarking)card).getPoison();
        }
        if (card instanceof TranquilWalkOfPeace) {
            return ((TranquilWalkOfPeace)card).baseStrength;
        }
        return card instanceof CrucibleToken ? ((CrucibleToken)card).basePoison : 0;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return (card instanceof PoisonMarking || card instanceof TranquilWalkOfPeace) && card.upgraded;
    }
}
