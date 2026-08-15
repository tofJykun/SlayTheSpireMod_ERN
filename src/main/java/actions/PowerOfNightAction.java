package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;

public class PowerOfNightAction extends AbstractGameAction {
    private static final int CHOICE_COUNT = 3;
    private boolean retrieveCard;

    public PowerOfNightAction() {
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

    private static ArrayList<AbstractCard> generateCardChoices() {
        ArrayList<AbstractCard> candidates = new ArrayList<>();
        addEligibleCards(candidates, AbstractDungeon.srcCommonCardPool.group);
        addEligibleCards(candidates, AbstractDungeon.srcUncommonCardPool.group);
        addEligibleCards(candidates, AbstractDungeon.srcRareCardPool.group);

        ArrayList<AbstractCard> choices = new ArrayList<>();
        int targetCount = Math.min(CHOICE_COUNT, candidates.size());
        while (choices.size() < targetCount) {
            int index = AbstractDungeon.cardRandomRng.random(candidates.size() - 1);
            choices.add(candidates.remove(index).makeCopy());
        }
        return choices;
    }

    private static void addEligibleCards(ArrayList<AbstractCard> target, ArrayList<AbstractCard> source) {
        for (AbstractCard card : source) {
            if (!card.hasTag(AbstractCard.CardTags.HEALING)) {
                target.add(card);
            }
        }
    }
}
