package variables;

import basemod.abstracts.DynamicVariable;
import cards.duchess.CrystalEnchanted;
import cards.duchess.EventualGreatness;
import cards.duchess.GodslayerGreatsword;
import cards.executor.RagingBeast;
import cards.guardian.SpinningGravityThrust;
import cards.guardian.DuelingShield;
import cards.ironeye.Usury;
import cards.raider.SmithingArtSpears;
import cards.tempcards.CrucibleToken;
import cards.wylder.WolfGreatsword;
import cards.undertaker.GreatStrength;
import cards.undertaker.GreatDexterity;
import cards.undertaker.DeepInsight;
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
        if (card instanceof SpinningGravityThrust) {
            return ((SpinningGravityThrust)card).getHoverLoss();
        }
        if (card instanceof SmithingArtSpears) {
            return ((SmithingArtSpears)card).getDamagePerSmithing();
        }
        if (card instanceof RagingBeast) {
            return ((RagingBeast)card).getMadnessAmount();
        }
        if (card instanceof GodslayerGreatsword) {
            return ((GodslayerGreatsword)card).getBloodburnApplications();
        }
        if (card instanceof GreatStrength || card instanceof GreatDexterity || card instanceof DeepInsight) {
            return 1;
        }
        if (card instanceof CrystalEnchanted) {
            return ((CrystalEnchanted)card).getStatGain();
        }
        if (card instanceof EventualGreatness) {
            return ((EventualGreatness)card).getStatLoss();
        }
        if (card instanceof WolfGreatsword) {
            return ((WolfGreatsword)card).getSignedDamage();
        }
        if (card instanceof Usury) {
            return ((Usury)card).getTokenDivisor();
        }
        if (card instanceof DuelingShield) {
            return 1;
        }
        return card instanceof CrucibleToken ? ((CrucibleToken)card).bloodburn : 0;
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof SpinningGravityThrust) {
            return ((SpinningGravityThrust)card).getHoverLoss();
        }
        if (card instanceof SmithingArtSpears) {
            return ((SmithingArtSpears)card).getDamagePerSmithing();
        }
        if (card instanceof RagingBeast) {
            return ((RagingBeast)card).getMadnessAmount();
        }
        if (card instanceof GodslayerGreatsword) {
            return ((GodslayerGreatsword)card).getBloodburnApplications();
        }
        if (card instanceof GreatStrength || card instanceof GreatDexterity || card instanceof DeepInsight) {
            return 1;
        }
        if (card instanceof CrystalEnchanted) {
            return ((CrystalEnchanted)card).getStatGain();
        }
        if (card instanceof EventualGreatness) {
            return ((EventualGreatness)card).getStatLoss();
        }
        if (card instanceof WolfGreatsword) {
            return ((WolfGreatsword)card).getSignedBaseDamage();
        }
        if (card instanceof Usury) {
            return ((Usury)card).getTokenDivisor();
        }
        if (card instanceof DuelingShield) {
            return 1;
        }
        return card instanceof CrucibleToken ? ((CrucibleToken)card).baseBloodburn : 0;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return card instanceof SmithingArtSpears && card.upgraded;
    }
}
