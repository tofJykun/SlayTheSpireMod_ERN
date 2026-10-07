package variables;

import basemod.abstracts.DynamicVariable;
import cards.ironeye.Marking;
import cards.executor.RiversOfBlood;
import cards.guardian.GlintstoneKris;
import cards.guardian.WingCrestShield;
import cards.guardian.CurseWardGreatshield;
import cards.guardian.GreatshieldOfGlory;
import cards.guardian.FalconShield;
import cards.guardian.FatalAppetite;
import cards.executor.AlabasterLordsPull;
import cards.executor.OnyxLordsRepulsion;
import cards.duchess.BladeOfCalling;
import cards.duchess.BlackKnife;
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
import cards.revenant.FamilyHeads;
import cards.revenant.SpiritInfusedFist;
import cards.wylder.IronFlesh;
import cards.wylder.Valorheart;
import com.megacrit.cardcrawl.cards.AbstractCard;
import patches.ScheduledField;

public class ScheduledVariable extends DynamicVariable {
    @Override
    public String key() {
        return "MS";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof FalconShield) {
            return false;
        }
        if (card instanceof GreatshieldOfGlory) {
            return false;
        }
        if (card instanceof CurseWardGreatshield) {
            return false;
        }
        if (card instanceof WingCrestShield) {
            return false;
        }
        if (card instanceof Valorheart) {
            return false;
        }
        if (card instanceof RiversOfBlood) {
            return false;
        }
        if (card instanceof GlintstoneKris) {
            return false;
        }
        if (card instanceof IronFlesh) {
            return false;
        }
        if (card instanceof FamilyHeads || card instanceof SpiritInfusedFist || card instanceof FatalAppetite) {
            return false;
        }
        if (card instanceof BlackKnife) {
            return false;
        }
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
        if (card instanceof FalconShield) {
            return ((FalconShield)card).getDrawAmount();
        }
        if (card instanceof GreatshieldOfGlory) {
            return ((GreatshieldOfGlory)card).getVigorAmount();
        }
        if (card instanceof CurseWardGreatshield) {
            return ((CurseWardGreatshield)card).getArtifactAmount();
        }
        if (card instanceof WingCrestShield) {
            return ((WingCrestShield)card).getHoverAmount();
        }
        if (card instanceof Valorheart) {
            return ((Valorheart)card).getParryAmount();
        }
        if (card instanceof RiversOfBlood) {
            return ((RiversOfBlood)card).getSelfBloodloss();
        }
        if (card instanceof GlintstoneKris) {
            return GlintstoneKris.STRENGTH_LOSS;
        }
        if (card instanceof IronFlesh) {
            return ((IronFlesh)card).getPlating();
        }
        if (card instanceof FamilyHeads || card instanceof SpiritInfusedFist || card instanceof FatalAppetite) {
            return 1;
        }
        if (card instanceof BlackKnife) {
            return ((BlackKnife)card).getBloodburn();
        }
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
        if (card instanceof FalconShield) {
            return ((FalconShield)card).getDrawAmount();
        }
        if (card instanceof GreatshieldOfGlory) {
            return ((GreatshieldOfGlory)card).getVigorAmount();
        }
        if (card instanceof CurseWardGreatshield) {
            return ((CurseWardGreatshield)card).getArtifactAmount();
        }
        if (card instanceof WingCrestShield) {
            return ((WingCrestShield)card).getHoverAmount();
        }
        if (card instanceof Valorheart) {
            return ((Valorheart)card).getParryAmount();
        }
        if (card instanceof RiversOfBlood) {
            return ((RiversOfBlood)card).getSelfBloodloss();
        }
        if (card instanceof GlintstoneKris) {
            return GlintstoneKris.STRENGTH_LOSS;
        }
        if (card instanceof IronFlesh) {
            return ((IronFlesh)card).getPlating();
        }
        if (card instanceof FamilyHeads || card instanceof SpiritInfusedFist || card instanceof FatalAppetite) {
            return 1;
        }
        if (card instanceof BlackKnife) {
            return ((BlackKnife)card).getBloodburn();
        }
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
        if (card instanceof WingCrestShield) {
            return card.upgraded;
        }
        if (card instanceof FalconShield) {
            return card.upgraded;
        }
        if (card instanceof GreatshieldOfGlory) {
            return card.upgraded;
        }
        if (card instanceof CurseWardGreatshield) {
            return card.upgraded;
        }
        return (card instanceof Valorheart || card instanceof RiversOfBlood || card instanceof IronFlesh || card instanceof BlackKnife || card instanceof LotOfRunes || card instanceof Marking || card instanceof Dodge) && card.upgraded;
    }
}
