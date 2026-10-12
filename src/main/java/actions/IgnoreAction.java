package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import patches.IgnoreField;

public class IgnoreAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final String prompt;
    private boolean selecting;

    public IgnoreAction(AbstractPlayer player, String prompt) {
        this.player = player;
        this.prompt = prompt;
        actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (isDone) return;
        if (player == null) {
            isDone = true;
            return;
        }
        if (!selecting) {
            if (player.hand.isEmpty()) {
                isDone = true;
            } else if (player.hand.size() == 1) {
                mark(player.hand.getTopCard());
                isDone = true;
            } else {
                CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                choices.group.addAll(player.hand.group);
                AbstractDungeon.gridSelectScreen.open(choices, 1, prompt, false, false, false, false);
                selecting = true;
            }
            return;
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            mark(AbstractDungeon.gridSelectScreen.selectedCards.get(0));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        isDone = true;
    }

    private void mark(AbstractCard card) {
        if (player.hand.contains(card)) {
            IgnoreField.ignore(card);
            card.unhover();
            card.flash();
            player.hand.refreshHandLayout();
        }
    }
}
