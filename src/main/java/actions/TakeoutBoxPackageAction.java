package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import general.PackagingCard;

import java.util.ArrayList;

public class TakeoutBoxPackageAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final PackagingCard box;
    private final int cardsToPackage;
    private final String prompt;

    public TakeoutBoxPackageAction(PackagingCard box, int cardsToPackage, String prompt) {
        this.player = AbstractDungeon.player;
        this.box = box;
        this.cardsToPackage = cardsToPackage;
        this.prompt = prompt;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.player == null || this.box == null || this.cardsToPackage <= 0 || this.player.hand.isEmpty()) {
                this.isDone = true;
                return;
            }

            int amount = Math.min(this.cardsToPackage, this.player.hand.size());
            if (this.player.hand.size() <= amount) {
                ArrayList<AbstractCard> cards = new ArrayList<AbstractCard>(this.player.hand.group);
                for (AbstractCard card : cards) {
                    this.player.hand.group.remove(card);
                }
                this.box.setPackagedCards(cards);
                this.player.hand.refreshHandLayout();
                this.isDone = true;
                return;
            }

            AbstractDungeon.handCardSelectScreen.open(this.prompt, amount,
                    false, false, false, false);
            tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            ArrayList<AbstractCard> cards = new ArrayList<AbstractCard>(
                    AbstractDungeon.handCardSelectScreen.selectedCards.group);
            for (AbstractCard card : cards) {
                this.player.hand.group.remove(card);
            }
            this.box.setPackagedCards(cards);
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            this.player.hand.refreshHandLayout();
            this.isDone = true;
            return;
        }

        tickDuration();
    }
}
