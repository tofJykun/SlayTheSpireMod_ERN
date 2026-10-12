package variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import general.ReplayBonus;
import patches.ReplayField;

public class ReplayVariable extends DynamicVariable {
    @Override
    public String key() { return "ERNReplay"; }

    @Override
    public boolean isModified(AbstractCard card) { return value(card) != baseValue(card); }

    @Override
    public int value(AbstractCard card) { return ReplayBonus.effective(card); }

    @Override
    public int baseValue(AbstractCard card) { return ReplayField.getReplay(card); }

    @Override
    public boolean upgraded(AbstractCard card) {
        return card.upgraded && ("Palestone".equals(card.cardID)
                || "DarkMoonGreatsword".equals(card.cardID) || "ScavengerCurvedSword".equals(card.cardID)
                || "Unveil".equals(card.cardID) || "Sidestep".equals(card.cardID)
                || "MushroomChips".equals(card.cardID));
    }
}
