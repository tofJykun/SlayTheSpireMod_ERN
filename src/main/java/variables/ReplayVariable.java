package variables;

import com.megacrit.cardcrawl.cards.AbstractCard;
import general.ReplayBonus;
import patches.ReplayField;

public class ReplayVariable extends UpgradePreviewVariable {
    @Override
    public String key() { return "ERNReplay"; }

    @Override
    protected boolean isValueModified(AbstractCard card) { return value(card) != baseValue(card); }

    @Override
    protected int currentValue(AbstractCard card) { return ReplayBonus.effective(card); }

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
