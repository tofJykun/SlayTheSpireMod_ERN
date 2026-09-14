package general;

import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;

public interface PackagingCard {
    ArrayList<AbstractCard> getPackagedCards();

    void setPackagedCards(ArrayList<AbstractCard> cards);

    void clearPackagedCards();

    default void refreshAfterPackagingUseAction() {
    }
}
