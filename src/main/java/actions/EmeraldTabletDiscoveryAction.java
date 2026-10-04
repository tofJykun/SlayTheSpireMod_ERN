package actions;

import basemod.BaseMod;
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
import powers.OperationeSolis;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class EmeraldTabletDiscoveryAction extends AbstractGameAction {
    private final boolean upgradedChoices;
    private boolean retrieveCard;

    public EmeraldTabletDiscoveryAction(boolean upgradedChoices) {
        this.upgradedChoices = upgradedChoices;
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            ArrayList<AbstractCard> choices = generateCardChoices();
            if (choices.isEmpty()) {
                isDone = true;
                return;
            }
            AbstractDungeon.cardRewardScreen.customCombatOpen(choices, CardRewardScreen.TEXT[1], true);
            tickDuration();
            return;
        }
        if (!retrieveCard) {
            AbstractCard selected = AbstractDungeon.cardRewardScreen.discoveryCard;
            if (selected != null) {
                AbstractCard card = selected.makeStatEquivalentCopy();
                if (!card.upgraded && AbstractDungeon.player.hasPower("MasterRealityPower")) {
                    card.upgrade();
                }
                card.current_x = -1000.0F * Settings.xScale;
                if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card,
                            Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                } else {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(card,
                            Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                }
                AbstractPower quest = AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
                if (quest instanceof OperationeSolis) {
                    // Resolve generation milestones before the card's final dialogue action.
                    ((OperationeSolis)quest).onEmeraldTabletCardGenerated(card);
                }
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
            }
            retrieveCard = true;
        }
        tickDuration();
    }

    private ArrayList<AbstractCard> generateCardChoices() {
        List<AbstractCard.CardColor> colors = Arrays.asList(AbstractCardEnum.Recluse_COLOR,
                AbstractCardEnum.Wylder_COLOR, AbstractCardEnum.Guardian_COLOR, AbstractCardEnum.Ironeye_COLOR,
                AbstractCardEnum.Raider_COLOR, AbstractCardEnum.Duchess_COLOR, AbstractCardEnum.Executor_COLOR,
                AbstractCardEnum.Revenant_COLOR, AbstractCardEnum.Scholar_COLOR, AbstractCardEnum.Undertaker_COLOR);
        TreeMap<String, AbstractCard> pool = new TreeMap<>();
        for (AbstractCard card : CardLibrary.cards.values()) {
            if (card.rarity == AbstractCard.CardRarity.COMMON && colors.contains(card.color)) {
                pool.put(card.cardID, card);
            }
        }
        ArrayList<AbstractCard> candidates = new ArrayList<>(pool.values());
        ArrayList<AbstractCard> choices = new ArrayList<>();
        while (choices.size() < 3 && !candidates.isEmpty()) {
            int index = AbstractDungeon.cardRandomRng.random(candidates.size() - 1);
            AbstractCard card = candidates.remove(index).makeCopy();
            if (upgradedChoices && card.canUpgrade()) {
                card.upgrade();
            }
            UnlockTracker.markCardAsSeen(card.cardID);
            choices.add(card);
        }
        return choices;
    }
}
