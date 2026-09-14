package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import general.SmithingHelper;

import java.util.ArrayList;

public class RetainSmithingMaterialCardsAction extends AbstractGameAction {
    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString("RetainCardsAction");
    private static final String[] TEXT = UI_STRINGS.TEXT;

    private final AbstractPlayer player;
    private final ArrayList<AbstractCard> hiddenCards = new ArrayList<>();
    private boolean opened;

    public RetainSmithingMaterialCardsAction(int amount) {
        this.player = AbstractDungeon.player;
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.player == null || this.amount <= 0 || this.player.hand.isEmpty()) {
            this.isDone = true;
            return;
        }

        if (!this.opened) {
            ArrayList<AbstractCard> materials = getMaterialCards();
            if (materials.isEmpty()) {
                this.isDone = true;
                return;
            }
            if (materials.size() <= this.amount) {
                for (AbstractCard card : materials) {
                    if (!card.isEthereal) {
                        card.retain = true;
                    }
                }
                this.player.hand.refreshHandLayout();
                this.isDone = true;
                return;
            }

            hideNonMaterialCards();
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, false, true, false, false, true);
            addToBot(new WaitAction(0.25F));
            this.opened = true;
            tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                if (!card.isEthereal) {
                    card.retain = true;
                }
                this.player.hand.addToTop(card);
            }
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            restoreHiddenCards();
            this.player.hand.refreshHandLayout();
            this.player.hand.applyPowers();
            this.isDone = true;
            return;
        }

        tickDuration();
    }

    private ArrayList<AbstractCard> getMaterialCards() {
        ArrayList<AbstractCard> materials = new ArrayList<>();
        for (AbstractCard card : this.player.hand.group) {
            if (SmithingHelper.isSmithingMaterial(card)) {
                materials.add(card);
            }
        }
        return materials;
    }

    private void hideNonMaterialCards() {
        ArrayList<AbstractCard> copy = new ArrayList<>(this.player.hand.group);
        for (AbstractCard card : copy) {
            if (!SmithingHelper.isSmithingMaterial(card)) {
                this.player.hand.group.remove(card);
                this.hiddenCards.add(card);
            }
        }
        this.player.hand.refreshHandLayout();
    }

    private void restoreHiddenCards() {
        for (AbstractCard card : this.hiddenCards) {
            if (!this.player.hand.group.contains(card)) {
                this.player.hand.addToBottom(card);
            }
        }
        this.hiddenCards.clear();
    }
}
