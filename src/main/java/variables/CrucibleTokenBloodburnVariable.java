package variables;

import basemod.abstracts.DynamicVariable;
import cards.duchess.CrystalEnchanted;
import cards.ironeye.Usury;
import cards.tempcards.CrucibleToken;
import cards.wylder.WolfGreatsword;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class CrucibleTokenBloodburnVariable extends DynamicVariable {
    @Override
    public String key() {
        return "MD";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return card instanceof WolfGreatsword
                && ((WolfGreatsword)card).getSignedDamage() != ((WolfGreatsword)card).getSignedBaseDamage();
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof CrystalEnchanted) {
            return ((CrystalEnchanted)card).getStatGain();
        }
        if (card instanceof WolfGreatsword) {
            return ((WolfGreatsword)card).getSignedDamage();
        }
        if (card instanceof Usury) {
            return ((Usury)card).getTokenDivisor();
        }
        return card instanceof CrucibleToken ? ((CrucibleToken)card).bloodburn : 0;
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof CrystalEnchanted) {
            return ((CrystalEnchanted)card).getStatGain();
        }
        if (card instanceof WolfGreatsword) {
            return ((WolfGreatsword)card).getSignedBaseDamage();
        }
        if (card instanceof Usury) {
            return ((Usury)card).getTokenDivisor();
        }
        return card instanceof CrucibleToken ? ((CrucibleToken)card).baseBloodburn : 0;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return false;
    }
}
