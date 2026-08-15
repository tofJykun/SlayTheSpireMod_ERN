package actions;

import cards.recluse.Chameleon;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;

public class ChameleonAction extends AbstractGameAction {
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(Chameleon.ID);

    private final AbstractPlayer player;
    private final int cardsToChoose;

    public ChameleonAction(int cardsToChoose) {
        this.player = AbstractDungeon.player;
        this.cardsToChoose = cardsToChoose;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.player == null || this.player.discardPile.isEmpty()) {
                this.isDone = true;
                return;
            }

            int amount = Math.min(this.cardsToChoose, this.player.discardPile.size());
            if (this.player.discardPile.size() <= this.cardsToChoose) {
                for (AbstractCard card : this.player.discardPile.group) {
                    addExhaustCopyToHand(card);
                }
                this.isDone = true;
                return;
            }

            AbstractDungeon.gridSelectScreen.open(this.player.discardPile, amount,
                    CARD_STRINGS.EXTENDED_DESCRIPTION[0], false, false, false, false);
            tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                addExhaustCopyToHand(card);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        this.isDone = true;
    }

    private void addExhaustCopyToHand(AbstractCard source) {
        AbstractCard copy = source.makeStatEquivalentCopy();
        boolean needsExhaustText = !copy.exhaust && copy.type != AbstractCard.CardType.POWER;
        copy.exhaust = true;
        if (needsExhaustText) {
            copy.rawDescription += CARD_STRINGS.EXTENDED_DESCRIPTION[1];
            copy.initializeDescription();
        }
        addToBot(new MakeTempCardInHandAction(copy, 1));
    }
}
