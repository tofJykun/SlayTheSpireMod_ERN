package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.ArrayList;

public class TransposingKilnAction extends AbstractGameAction {
    private static final int CHOICE_COUNT = 3;
    private static final int MAX_HAND_SIZE = 10;
    private final AbstractRelic relic;
    private final boolean deferred;
    private boolean retrieveCard;

    public TransposingKilnAction(AbstractRelic relic) {
        this(relic, false);
    }

    private TransposingKilnAction(AbstractRelic relic, boolean deferred) {
        this.relic = relic;
        this.deferred = deferred;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (!this.deferred) {
            AbstractDungeon.actionManager.addToBottom(new TransposingKilnAction(this.relic, true));
            this.isDone = true;
            return;
        }

        if (AbstractDungeon.player == null || AbstractDungeon.player.hand.size() >= MAX_HAND_SIZE) {
            this.isDone = true;
            return;
        }

        if (this.duration == Settings.ACTION_DUR_FAST) {
            ArrayList<AbstractCard> choices = generateCardChoices();
            if (choices.isEmpty()) {
                this.isDone = true;
                return;
            }
            if (this.relic != null) {
                this.relic.flash();
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(
                        (AbstractCreature)AbstractDungeon.player, this.relic));
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
                int amount = MAX_HAND_SIZE - AbstractDungeon.player.hand.size();
                if (amount > 0) {
                    AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(card, amount, false));
                }
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
            }
            this.retrieveCard = true;
        }
        tickDuration();
    }

    private static ArrayList<AbstractCard> generateCardChoices() {
        ArrayList<AbstractCard> candidates = new ArrayList<>();
        for (AbstractCard card : AbstractDungeon.srcCommonCardPool.group) {
            if (!card.hasTag(AbstractCard.CardTags.HEALING)) {
                candidates.add(card);
            }
        }

        ArrayList<AbstractCard> choices = new ArrayList<>();
        int targetCount = Math.min(CHOICE_COUNT, candidates.size());
        while (choices.size() < targetCount) {
            int index = AbstractDungeon.cardRandomRng == null
                    ? 0
                    : AbstractDungeon.cardRandomRng.random(candidates.size() - 1);
            AbstractCard card = candidates.remove(index).makeCopy();
            UnlockTracker.markCardAsSeen(card.cardID);
            choices.add(card);
        }
        return choices;
    }
}
