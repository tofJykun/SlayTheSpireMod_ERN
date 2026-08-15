package actions;

import cards.status.MagicEmber;
import cards.tempcards.FadingPrimalGlintstone;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class ArchiveOccultAction extends AbstractGameAction {
    private final AbstractPlayer player;

    public ArchiveOccultAction(AbstractPlayer player) {
        this.player = player;
        this.actionType = ActionType.EXHAUST;
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> cardsToExhaust = new ArrayList<>(this.player.hand.group);
        for (AbstractCard card : cardsToExhaust) {
            this.player.hand.moveToExhaustPile(card);
            AbstractCard generatedCard = AbstractDungeon.cardRandomRng.randomBoolean()
                    ? new FadingPrimalGlintstone()
                    : new MagicEmber();
            addToBot((AbstractGameAction)new MakeTempCardInHandAction(generatedCard, 1));
        }
        CardCrawlGame.dungeon.checkForPactAchievement();
        this.isDone = true;
    }
}
