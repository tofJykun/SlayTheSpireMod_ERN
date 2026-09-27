package variables;

import basemod.abstracts.DynamicVariable;
import cards.ironeye.Marking;
import cards.executor.AlabasterLordsPull;
import cards.executor.OnyxLordsRepulsion;
import cards.duchess.BladeOfCalling;
import cards.duchess.PowerOfTheGreaterWill;
import relics.VoiceConduit;
import cards.raider.LargeClub;
import cards.raider.FourProngedPlow;
import cards.raider.GreatClub;
import cards.raider.TotemStela;
import cards.recluse.GreatSoulDregs;
import cards.recluse.Dodge;
import cards.recluse.CarianPiercer;
import cards.revenant.LotOfRunes;
import com.megacrit.cardcrawl.cards.AbstractCard;
import patches.ScheduledField;

public class ScheduledVariable extends DynamicVariable {
    @Override
    public String key() {
        return "MS";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof CarianPiercer) {
            return false;
        }
        if (card instanceof Dodge) {
            return false;
        }
        if (card instanceof GreatSoulDregs) {
            return false;
        }
        if (card instanceof TotemStela) {
            return false;
        }
        if (card instanceof PowerOfTheGreaterWill) {
            return false;
        }
        if (card instanceof OnyxLordsRepulsion) {
            return false;
        }
        if (card instanceof AlabasterLordsPull) {
            return false;
        }
        if (card instanceof Marking) {
            return false;
        }
        if (card instanceof LotOfRunes) {
            return false;
        }
        if (card instanceof BladeOfCalling) {
            return ((BladeOfCalling)card).getBloodburn() != ((BladeOfCalling)card).getBaseBloodburn();
        }
        if (card instanceof FourProngedPlow) {
            return ((FourProngedPlow)card).getGreyHealth() != ((FourProngedPlow)card).getBaseGreyHealth();
        }
        if (card instanceof LargeClub) {
            return ((LargeClub)card).getGreyHealth() != ((LargeClub)card).getBaseGreyHealth();
        }
        if (card instanceof GreatClub) {
            return ((GreatClub)card).getGreyHealth() != ((GreatClub)card).getBaseGreyHealth();
        }
        return ScheduledField.getScheduled(card) != ScheduledField.getBaseScheduled(card);
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof CarianPiercer) {
            return CarianPiercer.GLINTSTONES;
        }
        if (card instanceof Dodge) {
            return ((Dodge)card).getVigor();
        }
        if (card instanceof GreatSoulDregs) {
            return GreatSoulDregs.MAGIC_EMBERS;
        }
        if (card instanceof TotemStela) {
            return TotemStela.STRENGTH;
        }
        if (card instanceof PowerOfTheGreaterWill) {
            return VoiceConduit.HEAL;
        }
        if (card instanceof OnyxLordsRepulsion) {
            return OnyxLordsRepulsion.HP_LOSS;
        }
        if (card instanceof AlabasterLordsPull) {
            return AlabasterLordsPull.HP_LOSS;
        }
        if (card instanceof Marking) {
            return ((Marking)card).getHealthLoss();
        }
        if (card instanceof LotOfRunes) {
            return ((LotOfRunes)card).getGreyHealth();
        }
        if (card instanceof BladeOfCalling) {
            return ((BladeOfCalling)card).getBloodburn();
        }
        if (card instanceof FourProngedPlow) {
            return ((FourProngedPlow)card).getGreyHealth();
        }
        if (card instanceof LargeClub) {
            return ((LargeClub)card).getGreyHealth();
        }
        if (card instanceof GreatClub) {
            return ((GreatClub)card).getGreyHealth();
        }
        return ScheduledField.getScheduled(card);
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof CarianPiercer) {
            return CarianPiercer.GLINTSTONES;
        }
        if (card instanceof Dodge) {
            return ((Dodge)card).getVigor();
        }
        if (card instanceof GreatSoulDregs) {
            return GreatSoulDregs.MAGIC_EMBERS;
        }
        if (card instanceof TotemStela) {
            return TotemStela.STRENGTH;
        }
        if (card instanceof PowerOfTheGreaterWill) {
            return VoiceConduit.HEAL;
        }
        if (card instanceof OnyxLordsRepulsion) {
            return OnyxLordsRepulsion.HP_LOSS;
        }
        if (card instanceof AlabasterLordsPull) {
            return AlabasterLordsPull.HP_LOSS;
        }
        if (card instanceof Marking) {
            return ((Marking)card).getHealthLoss();
        }
        if (card instanceof LotOfRunes) {
            return ((LotOfRunes)card).getGreyHealth();
        }
        if (card instanceof BladeOfCalling) {
            return ((BladeOfCalling)card).getBaseBloodburn();
        }
        if (card instanceof FourProngedPlow) {
            return ((FourProngedPlow)card).getBaseGreyHealth();
        }
        if (card instanceof LargeClub) {
            return ((LargeClub)card).getBaseGreyHealth();
        }
        if (card instanceof GreatClub) {
            return ((GreatClub)card).getBaseGreyHealth();
        }
        return ScheduledField.getBaseScheduled(card);
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return (card instanceof LotOfRunes || card instanceof Marking || card instanceof Dodge) && card.upgraded;
    }
}
