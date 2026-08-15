package actions;

import cards.ironeye.FellowshipAtt;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class FellowshipAttAction extends AbstractGameAction {
    private final String prompt;

    public FellowshipAttAction(String prompt) {
        this.prompt = prompt;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            CardGroup choices = buildChoices();
            if (choices.isEmpty()) {
                this.isDone = true;
                return;
            }
            AbstractDungeon.gridSelectScreen.open(choices, 1, this.prompt, false);
            tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard selected = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            addToTop((AbstractGameAction)new MakeTempCardInHandAction(selected.makeStatEquivalentCopy(), 1));
        }
        this.isDone = true;
    }

    private static CardGroup buildChoices() {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        for (Map.Entry<String, AbstractCard> entry : CardLibrary.cards.entrySet()) {
            AbstractCard card = entry.getValue();
            if (card.color == AbstractCardEnum.Ironeye_COLOR
                    && isEligibleRarity(card.rarity)
                    && !FellowshipAtt.ID.equals(card.cardID)) {
                cards.add(card.makeCopy());
            }
        }

        Collections.sort(cards, new Comparator<AbstractCard>() {
            @Override
            public int compare(AbstractCard left, AbstractCard right) {
                int rarityComparison = rarityRank(left.rarity) - rarityRank(right.rarity);
                if (rarityComparison != 0) {
                    return rarityComparison;
                }
                return left.cardID.compareTo(right.cardID);
            }
        });

        CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : cards) {
            UnlockTracker.markCardAsSeen(card.cardID);
            choices.addToBottom(card);
        }
        return choices;
    }

    private static boolean isEligibleRarity(AbstractCard.CardRarity rarity) {
        return rarity == AbstractCard.CardRarity.COMMON
                || rarity == AbstractCard.CardRarity.UNCOMMON
                || rarity == AbstractCard.CardRarity.RARE;
    }

    private static int rarityRank(AbstractCard.CardRarity rarity) {
        if (rarity == AbstractCard.CardRarity.COMMON) {
            return 0;
        }
        if (rarity == AbstractCard.CardRarity.UNCOMMON) {
            return 1;
        }
        return 2;
    }
}
