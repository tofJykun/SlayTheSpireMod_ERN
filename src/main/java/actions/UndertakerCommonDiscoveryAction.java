package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;

public class UndertakerCommonDiscoveryAction extends AbstractGameAction {
    private static final int CHOICE_COUNT = 3;
    private final boolean upgradedChoices;
    private boolean retrieveCard;

    public UndertakerCommonDiscoveryAction(boolean upgradedChoices) {
        this.upgradedChoices = upgradedChoices;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
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
                    ? 0 : AbstractDungeon.cardRandomRng.random(candidates.size() - 1);
            AbstractCard card = candidates.remove(index).makeCopy();
            if (this.upgradedChoices && card.canUpgrade()) {
                card.upgrade();
            }
            UnlockTracker.markCardAsSeen(card.cardID);
            choices.add(card);
        }
        return choices;
    }
}
