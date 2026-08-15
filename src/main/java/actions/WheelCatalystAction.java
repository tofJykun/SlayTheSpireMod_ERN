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

public class WheelCatalystAction extends AbstractGameAction {
    private boolean retrieveCard;

    public WheelCatalystAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            ArrayList<AbstractCard> choices = generateChoices();
            if (choices.isEmpty()) {
                this.isDone = true;
                return;
            }
            AbstractDungeon.cardRewardScreen.customCombatOpen(choices, CardRewardScreen.TEXT[1], true);
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

    private static ArrayList<AbstractCard> generateChoices() {
        ArrayList<AbstractCard> choices = new ArrayList<>();
        addRandomCardOfType(choices, AbstractCard.CardType.ATTACK);
        addRandomCardOfType(choices, AbstractCard.CardType.SKILL);
        addRandomCardOfType(choices, AbstractCard.CardType.POWER);
        return choices;
    }

    private static void addRandomCardOfType(ArrayList<AbstractCard> choices, AbstractCard.CardType type) {
        ArrayList<AbstractCard> candidates = getRecluseCardsOfType(type);
        if (candidates.isEmpty()) {
            return;
        }
        int index = AbstractDungeon.cardRandomRng == null
                ? 0
                : AbstractDungeon.cardRandomRng.random(candidates.size() - 1);
        AbstractCard card = candidates.get(index).makeCopy();
        UnlockTracker.markCardAsSeen(card.cardID);
        choices.add(card);
    }

    private static ArrayList<AbstractCard> getRecluseCardsOfType(AbstractCard.CardType type) {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        for (Map.Entry<String, AbstractCard> entry : CardLibrary.cards.entrySet()) {
            AbstractCard card = entry.getValue();
            if (card.color == AbstractCardEnum.Recluse_COLOR
                    && card.type == type
                    && card.rarity != AbstractCard.CardRarity.SPECIAL
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
}
