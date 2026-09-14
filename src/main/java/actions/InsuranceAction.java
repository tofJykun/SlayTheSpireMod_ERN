package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import patches.InsuranceField;

public class InsuranceAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final String prompt;
    private boolean selecting;

    public InsuranceAction(AbstractPlayer player, String prompt) {
        this.player = player;
        this.prompt = prompt;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.player == null) {
            this.isDone = true;
            return;
        }
        if (!this.selecting) {
            if (this.player.hand.isEmpty()) {
                this.isDone = true;
            } else if (this.player.hand.size() == 1) {
                insure(this.player.hand.getTopCard());
                this.isDone = true;
            } else {
                CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                choices.group.addAll(this.player.hand.group);
                AbstractDungeon.gridSelectScreen.open(choices, 1, this.prompt, false, false, false, false);
                this.selecting = true;
            }
            return;
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            insure(AbstractDungeon.gridSelectScreen.selectedCards.get(0));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        this.isDone = true;
    }

    private void insure(AbstractCard card) {
        if (this.player.hand.contains(card)) {
            InsuranceField.insure(card);
            card.unhover();
            card.flash();
            this.player.hand.refreshHandLayout();
        }
    }
}
