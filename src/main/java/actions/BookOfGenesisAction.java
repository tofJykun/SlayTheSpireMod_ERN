package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;

public class BookOfGenesisAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final boolean upgradedChoices;

    public BookOfGenesisAction(AbstractPlayer player, boolean upgradedChoices) {
        this.player = player;
        this.upgradedChoices = upgradedChoices;
        this.actionType = ActionType.EXHAUST;
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> cardsToExhaust = new ArrayList<>(this.player.hand.group);
        int exhaustedCount = 0;
        for (AbstractCard card : cardsToExhaust) {
            if (this.player.hand.contains(card)) {
                this.player.hand.moveToExhaustPile(card);
                exhaustedCount++;
            }
        }
        CardCrawlGame.dungeon.checkForPactAchievement();
        for (int i = 0; i < exhaustedCount; i++) {
            addToBot(new BookOfGenesisDiscoveryAction(this.upgradedChoices));
        }
        this.isDone = true;
    }

    private static class BookOfGenesisDiscoveryAction extends AbstractGameAction {
        private static final int CHOICE_COUNT = 3;
        private boolean retrieveCard;
        private final boolean upgradedChoices;

        private BookOfGenesisDiscoveryAction(boolean upgradedChoices) {
            this.actionType = ActionType.CARD_MANIPULATION;
            this.duration = Settings.ACTION_DUR_FAST;
            this.upgradedChoices = upgradedChoices;
        }

        @Override
        public void update() {
            if (this.duration == Settings.ACTION_DUR_FAST) {
                ArrayList<AbstractCard> choices = generateCardChoices();
                if (choices.isEmpty()) {
                    this.isDone = true;
                    return;
                }
                AbstractDungeon.cardRewardScreen.customCombatOpen(choices, CardRewardScreen.TEXT[1], false);
                tickDuration();
                return;
            }

            if (!this.retrieveCard) {
                AbstractCard selected = AbstractDungeon.cardRewardScreen.discoveryCard;
                if (selected != null) {
                    AbstractCard card = selected.makeStatEquivalentCopy();
                    if (!card.upgraded && AbstractDungeon.player.hasPower("MasterRealityPower")) {
                        card.upgrade();
                    }
                    card.current_x = -1000.0F * Settings.xScale;
                    if (AbstractDungeon.player.hand.size() < 10) {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card,
                                Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                    } else {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(card,
                                Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                    }
                    AbstractDungeon.cardRewardScreen.discoveryCard = null;
                }
                this.retrieveCard = true;
            }
            tickDuration();
        }

        private ArrayList<AbstractCard> generateCardChoices() {
            ArrayList<AbstractCard> choices = new ArrayList<>();
            int attempts = 0;
            while (choices.size() < CHOICE_COUNT && attempts < 200) {
                attempts++;
                AbstractCard card = AbstractDungeon.returnTrulyRandomCardInCombat().makeCopy();
                if (containsCard(choices, card.cardID)) {
                    continue;
                }
                if (this.upgradedChoices && card.canUpgrade()) {
                    card.upgrade();
                }
                UnlockTracker.markCardAsSeen(card.cardID);
                choices.add(card);
            }
            return choices;
        }

        private static boolean containsCard(ArrayList<AbstractCard> cards, String cardID) {
            for (AbstractCard card : cards) {
                if (card.cardID.equals(cardID)) {
                    return true;
                }
            }
            return false;
        }
    }
}
