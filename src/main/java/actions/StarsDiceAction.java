package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import general.TransformStats;

import java.util.ArrayList;
import java.util.Map;

public class StarsDiceAction extends AbstractGameAction {
    private static final String SELECT_CARD_ZH_PREFIX = "选择";
    private static final String SELECT_CARD_ZH_SUFFIX = "张牌变化。";
    private static final String SELECT_CARD_ENG_PREFIX = "Choose up to ";
    private static final String SELECT_CARD_ENG_SUFFIX = " card(s) to Transform.";

    private final AbstractPlayer player;
    private final int amount;
    private final boolean canPickZero;
    private boolean selected;

    public StarsDiceAction() {
        this(1);
    }

    public StarsDiceAction(int amount) {
        this(amount, true);
    }

    public StarsDiceAction(int amount, boolean canPickZero) {
        this.player = AbstractDungeon.player;
        this.amount = amount;
        this.canPickZero = canPickZero;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.player == null || this.amount <= 0) {
            this.isDone = true;
            return;
        }

        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.player.hand.isEmpty()) {
                this.isDone = true;
                return;
            }
            int selectableAmount = Math.min(this.amount, this.player.hand.size());
            String prompt = Settings.language == Settings.GameLanguage.ZHS
                    ? SELECT_CARD_ZH_PREFIX + selectableAmount + SELECT_CARD_ZH_SUFFIX
                    : (this.canPickZero ? SELECT_CARD_ENG_PREFIX : "Choose ") + selectableAmount + SELECT_CARD_ENG_SUFFIX;
            AbstractDungeon.handCardSelectScreen.open(prompt, selectableAmount,
                    this.canPickZero && selectableAmount > 1, this.canPickZero);
            tickDuration();
            return;
        }

        if (!this.selected) {
            if (!AbstractDungeon.handCardSelectScreen.selectedCards.isEmpty()) {
                ArrayList<AbstractCard> selectedCards = new ArrayList<>(AbstractDungeon.handCardSelectScreen.selectedCards.group);
                for (AbstractCard card : selectedCards) {
                    transform(card);
                }
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            this.player.hand.refreshHandLayout();
            this.selected = true;
        }
        tickDuration();
    }

    private void transform(AbstractCard original) {
        AbstractCard replacement = randomSameColorCard();
        if (replacement == null) {
            if (!this.player.hand.contains(original)) {
                this.player.hand.addToTop(original);
            }
            return;
        }

        this.player.hand.removeCard(original);
        if ((original.upgraded || this.player.hasPower("MasterRealityPower")) && replacement.canUpgrade()) {
            replacement.upgrade();
        }
        UnlockTracker.markCardAsSeen(replacement.cardID);
        TransformStats.recordTransform();
        replacement.current_x = -1000.0F * Settings.xScale;
        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(replacement,
                Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
    }

    private AbstractCard randomSameColorCard() {
        ArrayList<AbstractCard> candidates = new ArrayList<>();
        for (Map.Entry<String, AbstractCard> entry : CardLibrary.cards.entrySet()) {
            AbstractCard card = entry.getValue();
            if (card.color == this.player.getCardColor()
                    && isRewardRarity(card.rarity)
                    && card.type != AbstractCard.CardType.CURSE
                    && card.type != AbstractCard.CardType.STATUS
                    && !card.hasTag(AbstractCard.CardTags.HEALING)) {
                candidates.add(card);
            }
        }
        if (candidates.isEmpty()) {
            return null;
        }

        int index = AbstractDungeon.cardRandomRng == null
                ? 0
                : AbstractDungeon.cardRandomRng.random(candidates.size() - 1);
        return candidates.get(index).makeCopy();
    }

    private static boolean isRewardRarity(AbstractCard.CardRarity rarity) {
        return rarity == AbstractCard.CardRarity.COMMON
                || rarity == AbstractCard.CardRarity.UNCOMMON
                || rarity == AbstractCard.CardRarity.RARE;
    }
}
