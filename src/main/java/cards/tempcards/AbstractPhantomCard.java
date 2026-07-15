package cards.tempcards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;

public abstract class AbstractPhantomCard extends CustomCard {
    protected AbstractPhantomCard(String id, String name, String img, int cost, String rawDescription,
                                  AbstractCard.CardType type, AbstractCard.CardColor color,
                                  AbstractCard.CardRarity rarity, AbstractCard.CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }
}
