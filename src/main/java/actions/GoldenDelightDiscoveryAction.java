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
import java.util.HashSet;

public class GoldenDelightDiscoveryAction extends AbstractGameAction {
    private boolean opened;

    public GoldenDelightDiscoveryAction(int amount) {
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (isDone) return;
        if (AbstractDungeon.player == null || amount <= 0) {
            isDone = true;
            return;
        }
        if (!opened) {
            ArrayList<AbstractCard> choices = generateChoices();
            if (choices.isEmpty()) {
                isDone = true;
                return;
            }
            opened = true;
            AbstractDungeon.cardRewardScreen.customCombatOpen(choices, CardRewardScreen.TEXT[1], true);
            return;
        }

        AbstractCard selected = AbstractDungeon.cardRewardScreen.discoveryCard;
        if (selected != null) {
            // Reserve hand slots before the asynchronous creation effects finish.
            int slots = Math.max(0, 10 - AbstractDungeon.player.hand.size());
            for (int i = 0; i < amount; i++) {
                AbstractCard card = selected.makeStatEquivalentCopy();
                if (!card.upgraded && AbstractDungeon.player.hasPower("MasterRealityPower")) {
                    card.upgrade();
                }
                card.setCostForTurn(0);
                card.current_x = -1000.0F * Settings.xScale;
                float x = Settings.WIDTH / 2.0F + (i - (amount - 1) / 2.0F) * AbstractCard.IMG_WIDTH;
                if (i < slots) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card, x, Settings.HEIGHT / 2.0F));
                } else {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(card, x, Settings.HEIGHT / 2.0F));
                }
            }
            AbstractDungeon.cardRewardScreen.discoveryCard = null;
        }
        isDone = true;
    }

    private ArrayList<AbstractCard> generateChoices() {
        ArrayList<AbstractCard> candidates = new ArrayList<>();
        HashSet<String> ids = new HashSet<>();
        for (AbstractCard card : AbstractDungeon.srcRareCardPool.group) {
            if (card.rarity == AbstractCard.CardRarity.RARE
                    && !card.hasTag(AbstractCard.CardTags.HEALING) && ids.add(card.cardID)) {
                candidates.add(card);
            }
        }
        ArrayList<AbstractCard> choices = new ArrayList<>();
        int count = Math.min(3, candidates.size());
        for (int i = 0; i < count; i++) {
            AbstractCard card = candidates.remove(AbstractDungeon.cardRandomRng.random(candidates.size() - 1)).makeCopy();
            UnlockTracker.markCardAsSeen(card.cardID);
            choices.add(card);
        }
        return choices;
    }
}
