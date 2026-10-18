package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import patches.SowingField;

public class SowingAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final String prompt;
    private boolean selecting;

    public SowingAction(AbstractPlayer player, String prompt) {
        this.player = player;
        this.prompt = prompt;
        actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (player == null) { isDone = true; return; }
        if (!selecting) {
            CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard card : player.hand.group) {
                if (!SowingField.isSown(card)) choices.group.add(card);
            }
            if (choices.isEmpty()) {
                isDone = true;
            } else if (choices.size() == 1) {
                sow(choices.getTopCard());
                isDone = true;
            } else {
                AbstractDungeon.gridSelectScreen.open(choices, 1, prompt, false, false, false, false);
                selecting = true;
            }
            return;
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            sow(AbstractDungeon.gridSelectScreen.selectedCards.get(0));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        isDone = true;
    }

    private void sow(AbstractCard card) {
        if (player.hand.contains(card) && !SowingField.isSown(card)) {
            SowingField.sow(card);
            card.unhover();
            card.flash();
            player.hand.refreshHandLayout();
        }
    }
}
