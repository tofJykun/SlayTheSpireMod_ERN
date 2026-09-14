package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class RecluseDiscoveryAction extends AbstractGameAction {
    private static final int CHOICE_COUNT = 3;
    private final boolean upgradedChoices;
    private boolean retrieveCard;

    public RecluseDiscoveryAction(boolean upgradedChoices) {
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
                card.setCostForTurn(0);
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
        ArrayList<AbstractCard> candidates = getRecluseCards();
        ArrayList<AbstractCard> choices = new ArrayList<>();
        int targetCount = Math.min(CHOICE_COUNT, candidates.size());
        while (choices.size() < targetCount) {
            int index = AbstractDungeon.cardRandomRng.random(candidates.size() - 1);
            AbstractCard card = candidates.remove(index).makeCopy();
            if (this.upgradedChoices && card.canUpgrade()) {
                card.upgrade();
            }
            UnlockTracker.markCardAsSeen(card.cardID);
            choices.add(card);
        }
        return choices;
    }

    private static ArrayList<AbstractCard> getRecluseCards() {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        for (Map.Entry<String, AbstractCard> entry : CardLibrary.cards.entrySet()) {
            AbstractCard card = entry.getValue();
            if (card.color == AbstractCardEnum.Recluse_COLOR
                    && isRewardRarity(card.rarity)
                    && card.type != AbstractCard.CardType.CURSE
                    && card.type != AbstractCard.CardType.STATUS
                    && !card.hasTag(AbstractCard.CardTags.HEALING)) {
                cards.add(card);
            }
        }
        Collections.sort(cards, new Comparator<AbstractCard>() {
            @Override
            public int compare(AbstractCard a, AbstractCard b) {
                return a.cardID.compareTo(b.cardID);
            }
        });
        return cards;
    }

    private static boolean isRewardRarity(AbstractCard.CardRarity rarity) {
        return rarity == AbstractCard.CardRarity.COMMON
                || rarity == AbstractCard.CardRarity.UNCOMMON
                || rarity == AbstractCard.CardRarity.RARE;
    }
}
