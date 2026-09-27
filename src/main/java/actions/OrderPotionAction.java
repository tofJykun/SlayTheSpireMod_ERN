package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public class OrderPotionAction extends AbstractGameAction {
    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString("OrderPotion");

    private final AbstractPlayer player;
    private final int discardAmount;

    public OrderPotionAction(int discardAmount) {
        this.player = AbstractDungeon.player;
        this.discardAmount = discardAmount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.player == null || this.player.hand.isEmpty() || this.discardAmount <= 0) {
                this.isDone = true;
                return;
            }

            int amount = Math.min(this.discardAmount, this.player.hand.size());
            AbstractDungeon.handCardSelectScreen.open(UI_STRINGS.TEXT[0], amount,
                    false, false, false, false);
            tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            ArrayList<AbstractCard> selected = new ArrayList<>(
                    AbstractDungeon.handCardSelectScreen.selectedCards.group);
            for (AbstractCard card : selected) {
                this.player.hand.moveToDiscardPile(card);
                GameActionManager.incrementDiscard(false);
                card.triggerOnManualDiscard();
            }
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            this.player.hand.refreshHandLayout();
            this.player.hand.applyPowers();
            this.isDone = true;
            return;
        }

        tickDuration();
    }
}
