package actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.util.ArrayList;
import java.util.Comparator;

public class GateOfHornAction extends AbstractGameAction {
    private final boolean upgraded;

    public GateOfHornAction(boolean upgraded) {
        this.upgraded = upgraded;
        actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        isDone = true;
        if (AbstractDungeon.player == null) {
            return;
        }
        int spaces = BaseMod.MAX_HAND_SIZE - AbstractDungeon.player.hand.size();
        if (spaces <= 0) {
            return;
        }

        ArrayList<AbstractCard> candidates = new ArrayList<>();
        for (AbstractCard prototype : CardLibrary.cards.values()) {
            if (prototype.color != AbstractDungeon.player.getCardColor()) {
                continue;
            }
            AbstractCard card = prototype.makeCopy();
            if (upgraded) {
                if (!card.canUpgrade()) {
                    continue;
                }
                card.upgrade();
            }
            if (card.cost == 0) {
                candidates.add(card);
            }
        }
        if (candidates.isEmpty()) {
            return;
        }
        // Library map iteration must not change the seeded selection order.
        candidates.sort(Comparator.comparing(card -> card.cardID));
        ArrayList<AbstractCard> generated = new ArrayList<>();
        for (int i = 0; i < spaces; i++) {
            int index = AbstractDungeon.cardRandomRng.random(candidates.size() - 1);
            generated.add(candidates.get(index).makeStatEquivalentCopy());
        }
        // Queue in selection order; reserve only the slots available at resolution.
        for (int i = generated.size() - 1; i >= 0; i--) {
            addToTop(new MakeTempCardInHandAction(generated.get(i), 1));
        }
    }
}
