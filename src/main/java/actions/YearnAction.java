package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class YearnAction extends AbstractGameAction {
    private static final String SELECT_DISCARD_ZH = "选择弃牌堆中要交换的牌。最多还能选择 ";
    private static final String SELECT_DRAW_ZH = "选择抽牌堆中相同数量的牌。还需选择 ";
    private static final String CARDS_ZH = " 张牌。";
    private static final String SELECT_DISCARD_ENG = "Choose cards from your discard pile to swap. Up to ";
    private static final String SELECT_DRAW_ENG = "Choose the same number of cards from your draw pile. Choose ";
    private static final String CARDS_ENG = " card(s).";

    private final AbstractPlayer player;
    private final boolean anyNumber;
    private final ArrayList<AbstractCard> discardCards = new ArrayList<>();
    private final ArrayList<Integer> discardIndexes = new ArrayList<>();
    private final ArrayList<AbstractCard> drawCards = new ArrayList<>();
    private final ArrayList<Integer> drawIndexes = new ArrayList<>();
    private int step = 0;

    public YearnAction(boolean anyNumber) {
        this.player = AbstractDungeon.player;
        this.anyNumber = anyNumber;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FASTER;
    }

    @Override
    public void update() {
        if (this.player == null || maxSelectableCards() <= 0) {
            this.isDone = true;
            return;
        }

        if (this.step == 0) {
            openDiscardSelection();
            return;
        }

        if (this.step == 1) {
            saveDiscardSelection();
            if (this.discardCards.isEmpty()) {
                this.isDone = true;
                return;
            }
            openDrawSelection();
            return;
        }

        if (this.step == 2) {
            saveDrawSelection();
            if (this.drawCards.size() == this.discardCards.size()) {
                swapSelectedCards();
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.isDone = true;
        }
    }

    private void openDiscardSelection() {
        int amount = maxSelectableCards();
        if (amount <= 0) {
            this.isDone = true;
            return;
        }

        if (!this.anyNumber && this.player.discardPile.size() == 1) {
            rememberDiscardCard(this.player.discardPile.getTopCard());
            this.step = 1;
            return;
        }

        if (this.anyNumber) {
            AbstractDungeon.gridSelectScreen.open(this.player.discardPile, amount, true, discardPrompt(amount));
        } else {
            AbstractDungeon.gridSelectScreen.open(this.player.discardPile, amount, discardPrompt(amount), false, false, false, false);
        }
        this.step = 1;
    }

    private void saveDiscardSelection() {
        if (this.discardCards.isEmpty()) {
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                rememberDiscardCard(card);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    private void openDrawSelection() {
        int amount = this.discardCards.size();
        if (this.player.drawPile.size() == amount) {
            for (AbstractCard card : this.player.drawPile.group) {
                rememberDrawCard(card);
            }
            this.step = 2;
            return;
        }

        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : this.player.drawPile.group) {
            tmp.addToTop(card);
        }
        AbstractDungeon.gridSelectScreen.open(tmp, amount, drawPrompt(amount), false);
        this.step = 2;
    }

    private void saveDrawSelection() {
        if (this.drawCards.isEmpty()) {
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                rememberDrawCard(card);
            }
        }
    }

    private void rememberDiscardCard(AbstractCard card) {
        this.discardCards.add(card);
        this.discardIndexes.add(this.player.discardPile.group.indexOf(card));
    }

    private void rememberDrawCard(AbstractCard card) {
        this.drawCards.add(card);
        this.drawIndexes.add(this.player.drawPile.group.indexOf(card));
    }

    private void swapSelectedCards() {
        ArrayList<AbstractCard> newDiscardPile = new ArrayList<>(this.player.discardPile.group);
        ArrayList<AbstractCard> newDrawPile = new ArrayList<>(this.player.drawPile.group);

        for (int i = 0; i < this.discardCards.size(); i++) {
            newDrawPile.set(this.drawIndexes.get(i), this.discardCards.get(i));
            newDiscardPile.set(this.discardIndexes.get(i), this.drawCards.get(i));
            this.discardCards.get(i).unhover();
            this.drawCards.get(i).unhover();
        }

        this.player.discardPile.group.clear();
        this.player.discardPile.group.addAll(newDiscardPile);
        this.player.drawPile.group.clear();
        this.player.drawPile.group.addAll(newDrawPile);
        if (this.drawIndexes.contains(newDrawPile.size() - 1)) {
            patches.OperationeSolisPatch.recordTopPlacement(this.player.drawPile);
        }
    }

    private int maxSelectableCards() {
        int pileLimit = Math.min(this.player.discardPile.size(), this.player.drawPile.size());
        return this.anyNumber ? pileLimit : Math.min(1, pileLimit);
    }

    private String discardPrompt(int amount) {
        if (Settings.language == Settings.GameLanguage.ZHS) {
            return SELECT_DISCARD_ZH + amount + CARDS_ZH;
        }
        return SELECT_DISCARD_ENG + amount + CARDS_ENG;
    }

    private String drawPrompt(int amount) {
        if (Settings.language == Settings.GameLanguage.ZHS) {
            return SELECT_DRAW_ZH + amount + CARDS_ZH;
        }
        return SELECT_DRAW_ENG + amount + CARDS_ENG;
    }
}
