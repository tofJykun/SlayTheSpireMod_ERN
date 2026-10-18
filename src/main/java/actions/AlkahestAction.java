package actions;

import cards.scholar.Alkahest;
import cards.scholar.Prakriti;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import general.CombatState;
import java.util.ArrayList;

public class AlkahestAction extends AbstractGameAction {
    private final Alkahest sourceCard;

    public AlkahestAction() { this(null); }

    private AlkahestAction(Alkahest sourceCard) {
        this.sourceCard = sourceCard;
        actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (isDone) return;
        isDone = true;
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null || !CombatState.isInCombat() || AbstractDungeon.getCurrRoom().isBattleOver) return;
        if (sourceCard == null) {
            // Inspect the hand after the opening draw has resolved, not when it is queued.
            for (AbstractCard card : p.hand.group) {
                if (card instanceof Alkahest) {
                    addToTop(new AlkahestAction((Alkahest) card));
                    addToTop(new DiscardSpecificCardAction(card));
                    break;
                }
            }
            return;
        }
        int exhausted = 0;
        for (AbstractCard card : new ArrayList<>(p.hand.group)) {
            if (p.hand.contains(card)) {
                p.hand.moveToExhaustPile(card);
                exhausted++;
            }
        }
        CardCrawlGame.dungeon.checkForPactAchievement();
        if (exhausted > 0) {
            Prakriti result = new Prakriti();
            if (sourceCard.upgraded) result.upgrade();
            addToTop(new MakeTempCardInHandAction(result, exhausted));
        }
    }
}
