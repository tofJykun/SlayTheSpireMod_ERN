package actions;

import cards.undertaker.DeepInsight;
import cards.undertaker.GreatDexterity;
import cards.undertaker.GreatStrength;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;

import java.util.ArrayList;

public class MakeYourChoiceAction extends AbstractGameAction {
    private final boolean upgraded;
    private boolean resolved;

    public MakeYourChoiceAction(boolean upgraded) {
        this.upgraded = upgraded;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            ArrayList<AbstractCard> choices = new ArrayList<>();
            choices.add(makeChoice(new GreatStrength()));
            choices.add(makeChoice(new GreatDexterity()));
            choices.add(makeChoice(new DeepInsight()));
            AbstractDungeon.cardRewardScreen.customCombatOpen(choices, CardRewardScreen.TEXT[1], false);
            tickDuration();
            return;
        }
        if (!this.resolved) {
            AbstractCard selected = AbstractDungeon.cardRewardScreen.discoveryCard;
            if (selected != null) {
                selected.use(AbstractDungeon.player, null);
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
            }
            this.resolved = true;
        }
        tickDuration();
    }

    private AbstractCard makeChoice(AbstractCard card) {
        if (this.upgraded && card.canUpgrade()) {
            card.upgrade();
        }
        return card;
    }
}
