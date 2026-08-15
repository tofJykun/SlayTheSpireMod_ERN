package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import general.CrystalCardHelper;

public class CrystallizationAction extends AbstractGameAction {
    private static final String SELECT_CARD_ZH = "选择1张牌消耗。";
    private static final String SELECT_CARD_ENG = "Choose 1 card to Exhaust.";

    private final AbstractPlayer player;

    public CrystallizationAction() {
        this.player = AbstractDungeon.player;
        this.actionType = ActionType.EXHAUST;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.player.hand.isEmpty()) {
                addRandomCrystalCard();
                this.isDone = true;
                return;
            }

            if (this.player.hand.size() == 1) {
                AbstractCard card = this.player.hand.getTopCard();
                this.player.hand.moveToExhaustPile(card);
                CardCrawlGame.dungeon.checkForPactAchievement();
                addRandomCrystalCard();
                this.isDone = true;
                return;
            }

            AbstractDungeon.handCardSelectScreen.open(
                    Settings.language == Settings.GameLanguage.ZHS ? SELECT_CARD_ZH : SELECT_CARD_ENG,
                    1, false, false, false, false);
            tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                this.player.hand.moveToExhaustPile(card);
            }
            CardCrawlGame.dungeon.checkForPactAchievement();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.player.hand.refreshHandLayout();
            addRandomCrystalCard();
            this.isDone = true;
        }

        tickDuration();
    }

    private void addRandomCrystalCard() {
        AbstractCard card = CrystalCardHelper.randomCrystalCard();
        if (card != null) {
            addToTop(new MakeTempCardInHandAction(card, 1));
        }
    }
}
