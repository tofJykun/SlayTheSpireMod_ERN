package actions;

import cards.tempcards.AbstractPhantomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class HorizontalAllianceAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final AbstractGameAction[] followUp;
    private final String prompt;
    private boolean selecting;

    public HorizontalAllianceAction(AbstractPlayer player, int drawAmount, String prompt) {
        this(player, prompt, new DrawCardAction(player, drawAmount));
    }

    public HorizontalAllianceAction(AbstractPlayer player, String prompt, AbstractGameAction... followUp) {
        this.player = player;
        this.followUp = followUp;
        this.prompt = prompt;
        actionType = ActionType.CARD_MANIPULATION;
    }

    public static boolean hasSlash(AbstractPlayer player) {
        if (player == null) return false;
        for (AbstractCard card : player.hand.group) {
            if (card instanceof AbstractPhantomCard) return true;
        }
        return false;
    }

    @Override
    public void update() {
        if (isDone) return;
        if (player == null) {
            isDone = true;
            return;
        }
        if (!selecting) {
            CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard card : player.hand.group) {
                if (card instanceof AbstractPhantomCard) choices.group.add(card);
            }
            if (choices.isEmpty()) {
                isDone = true;
            } else if (choices.size() == 1) {
                exhaustAndContinue(choices.getTopCard());
            } else {
                AbstractDungeon.gridSelectScreen.open(choices, 1, prompt, false, false, false, false);
                selecting = true;
            }
            return;
        }
        if (AbstractDungeon.isScreenUp) return;
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard selected = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            exhaustAndContinue(selected);
        } else {
            isDone = true;
        }
    }

    private void exhaustAndContinue(AbstractCard card) {
        if (card instanceof AbstractPhantomCard && player.hand.contains(card)) {
            card.unhover();
            for (int i = followUp.length - 1; i >= 0; i--) {
                addToTop(followUp[i]);
            }
            addToTop(new ExhaustSpecificCardAction(card, player.hand));
        }
        isDone = true;
    }
}
