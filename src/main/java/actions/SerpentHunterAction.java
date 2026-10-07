package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SerpentHunterAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int plays;
    private final String prompt;
    private boolean selecting;

    public SerpentHunterAction(int plays, String prompt) {
        this.player = AbstractDungeon.player;
        this.plays = plays;
        this.prompt = prompt;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.isDone) {
            return;
        }
        if (!this.selecting) {
            CardGroup attacks = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            if (this.player != null) {
                for (AbstractCard card : this.player.hand.group) {
                    if (card.type == AbstractCard.CardType.ATTACK) {
                        attacks.addToBottom(card);
                    }
                }
            }
            if (attacks.isEmpty()) {
                this.isDone = true;
            } else if (attacks.size() == 1) {
                playAndExhaust(attacks.getTopCard());
                this.isDone = true;
            } else {
                this.selecting = true;
                AbstractDungeon.gridSelectScreen.open(attacks, 1, this.prompt, false, false, false, false);
            }
            return;
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard selected = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            playAndExhaust(selected);
            this.isDone = true;
        }
    }

    private void playAndExhaust(AbstractCard card) {
        if (!this.player.hand.group.contains(card)) {
            return;
        }
        if (this.plays <= 0) {
            this.player.hand.moveToExhaustPile(card);
        } else {
            // Like Omniscience, extra plays are disposable copies. Play the original
            // last so its normal exhaust callbacks run after the repeated attacks.
            for (int i = 1; i < this.plays; i++) {
                AbstractCard copy = card.makeStatEquivalentCopy();
                copy.purgeOnUse = true;
                addToBot(new NewQueueCardAction(copy, true, false, true));
            }
            card.exhaustOnUseOnce = true;
            AvelynAction.rememberHandPlay(card);
            this.player.hand.group.remove(card);
            AbstractDungeon.getCurrRoom().souls.remove(card);
            addToBot(new NewQueueCardAction(card, true, false, true));
        }
        this.player.hand.refreshHandLayout();
        this.player.hand.applyPowers();
    }
}
