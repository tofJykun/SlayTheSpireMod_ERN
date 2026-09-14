package variables;

import basemod.abstracts.DynamicVariable;
import cards.duchess.BladeOfCalling;
import cards.raider.LargeClub;
import cards.raider.FourProngedPlow;
import cards.raider.GreatClub;
import com.megacrit.cardcrawl.cards.AbstractCard;
import patches.ScheduledField;

public class ScheduledVariable extends DynamicVariable {
    @Override
    public String key() {
        return "MS";
    }

    @Override
    public boolean isModified(AbstractCard card) {
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
        return false;
    }
}
