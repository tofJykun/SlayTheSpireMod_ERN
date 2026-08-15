package actions;

import cards.scholar.Homunculus;
import cards.scholar.PenglaiWorship;
import cards.scholar.RoyalLegacy;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;

public class AjaRedStoneAction extends AbstractGameAction {
    private boolean retrieveCard;

    public AjaRedStoneAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.cardRewardScreen.customCombatOpen(generateCardChoices(), CardRewardScreen.TEXT[1], false);
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

    private static ArrayList<AbstractCard> generateCardChoices() {
        ArrayList<AbstractCard> choices = new ArrayList<>();
        addChoice(choices, new RoyalLegacy());
        addChoice(choices, new PenglaiWorship());
        addChoice(choices, new Homunculus());
        return choices;
    }

    private static void addChoice(ArrayList<AbstractCard> choices, AbstractCard card) {
        UnlockTracker.markCardAsSeen(card.cardID);
        choices.add(card);
    }
}
