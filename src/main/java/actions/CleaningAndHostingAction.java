package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class CleaningAndHostingAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final String prompt;

    public CleaningAndHostingAction(String prompt) {
        this.player = AbstractDungeon.player;
        this.prompt = prompt;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.player == null || this.player.hand.isEmpty()) {
                this.isDone = true;
                return;
            }

            AbstractDungeon.handCardSelectScreen.open(this.prompt, 99, true, true);
            addToBot(new WaitAction(0.25F));
            tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            int discarded = AbstractDungeon.handCardSelectScreen.selectedCards.group.size();
            if (discarded > 0) {
                addToTop(new DrawCardAction(this.player, discarded));
                for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    this.player.hand.moveToDiscardPile(card);
                    GameActionManager.incrementDiscard(false);
                    card.triggerOnManualDiscard();
                }
                this.player.hand.refreshHandLayout();
                this.player.hand.applyPowers();
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.isDone = true;
            return;
        }

        tickDuration();
    }
}
