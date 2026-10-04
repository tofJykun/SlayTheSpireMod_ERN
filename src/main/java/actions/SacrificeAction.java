package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class SacrificeAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private boolean selectionOpened;

    public SacrificeAction(AbstractPlayer player) {
        this.player = player;
        actionType = ActionType.EXHAUST;
    }

    @Override
    public void update() {
        if (isDone) {
            return;
        }
        if (!selectionOpened) {
            if (player == null || player.hand.isEmpty()) {
                isDone = true;
                return;
            }
            selectionOpened = true;
            AbstractDungeon.handCardSelectScreen.open(ExhaustAction.TEXT[0], player.hand.size(), true, true);
            return;
        }
        if (AbstractDungeon.isScreenUp) {
            return;
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            // Exhaust hooks can return or generate cards, so count the selected originals.
            ArrayList<AbstractCard> selected = new ArrayList<>(AbstractDungeon.handCardSelectScreen.selectedCards.group);
            for (AbstractCard card : selected) {
                player.hand.moveToExhaustPile(card);
            }
            CardCrawlGame.dungeon.checkForPactAchievement();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            if (!selected.isEmpty()) {
                addToBot(new DamageAction(player, new DamageInfo(player, selected.size(), DamageInfo.DamageType.THORNS),
                        AttackEffect.FIRE));
            }
        }
        isDone = true;
    }
}
