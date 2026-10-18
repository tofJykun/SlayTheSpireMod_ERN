package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import java.util.ArrayList;

public class RandomCardsAction extends AbstractGameAction {
    public RandomCardsAction(int amount) {
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (isDone) return;
        isDone = true;
        if (AbstractDungeon.player == null || amount <= 0) return;
        ArrayList<AbstractCard> candidates = new ArrayList<>();
        // Match Discovery's combined pool/order, while allowing an empty modded pool.
        for (CardGroup pool : new CardGroup[] {AbstractDungeon.srcCommonCardPool,
                AbstractDungeon.srcUncommonCardPool, AbstractDungeon.srcRareCardPool}) {
            for (AbstractCard card : pool.group) {
                if (!card.hasTag(AbstractCard.CardTags.HEALING) && !general.ExclusiveCardPool.isExcluded(card)) {
                    candidates.add(card);
                    UnlockTracker.markCardAsSeen(card.cardID);
                }
            }
        }
        if (candidates.isEmpty()) return;
        ArrayList<AbstractCard> generated = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            int index = AbstractDungeon.cardRandomRng == null
                    ? 0 : AbstractDungeon.cardRandomRng.random(candidates.size() - 1);
            generated.add(candidates.get(index).makeCopy());
        }
        // Prepend in reverse so cards enter the hand in RNG selection order.
        for (int i = generated.size() - 1; i >= 0; i--) {
            addToTop(new MakeTempCardInHandAction(generated.get(i), 1));
        }
    }
}
