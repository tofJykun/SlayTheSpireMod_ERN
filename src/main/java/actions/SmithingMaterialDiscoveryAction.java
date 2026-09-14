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
import general.SmithingHelper;

import java.util.ArrayList;

public class SmithingMaterialDiscoveryAction extends AbstractGameAction {
    private static final int CHOICE_COUNT = 3;
    private boolean retrieveCard;

    public SmithingMaterialDiscoveryAction() {
        this(1);
    }

    public SmithingMaterialDiscoveryAction(int amount) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = Math.max(1, amount);
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            ArrayList<AbstractCard> choices = generateCardChoices();
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
                int availableSlots = Math.max(0, 10 - AbstractDungeon.player.hand.size());
                for (int i = 0; i < this.amount; i++) {
                    AbstractCard card = selected.makeStatEquivalentCopy();
                    if (!card.upgraded && AbstractDungeon.player.hasPower("MasterRealityPower")) {
                        card.upgrade();
                    }
                    card.current_x = -1000.0F * Settings.xScale;
                    float x = Settings.WIDTH / 2.0F + (i - (this.amount - 1) / 2.0F) * AbstractCard.IMG_WIDTH;
                    if (i < availableSlots) {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card,
                                x, Settings.HEIGHT / 2.0F));
                    } else {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(card,
                                x, Settings.HEIGHT / 2.0F));
                    }
                }
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
            }
            this.retrieveCard = true;
        }
        tickDuration();
    }

    private static ArrayList<AbstractCard> generateCardChoices() {
        ArrayList<String> candidates = new ArrayList<>(SmithingHelper.getSmithingMaterialIds());
        ArrayList<AbstractCard> choices = new ArrayList<>();
        int targetCount = Math.min(CHOICE_COUNT, candidates.size());
        while (choices.size() < targetCount && !candidates.isEmpty()) {
            int index = AbstractDungeon.cardRandomRng == null
                    ? 0
                    : AbstractDungeon.cardRandomRng.random(candidates.size() - 1);
            AbstractCard template = CardLibrary.getCard(candidates.remove(index));
            if (template != null) {
                AbstractCard card = template.makeCopy();
                UnlockTracker.markCardAsSeen(card.cardID);
                choices.add(card);
            }
        }
        return choices;
    }
}
