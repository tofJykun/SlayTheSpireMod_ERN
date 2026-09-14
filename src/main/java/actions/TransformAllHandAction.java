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

public class TransformAllHandAction extends AbstractGameAction {
    private final AbstractPlayer player;

    public TransformAllHandAction() {
        this.player = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.player == null || this.player.hand.isEmpty()) {
            this.isDone = true;
            return;
        }

        ArrayList<AbstractCard> handSnapshot = new ArrayList<>(this.player.hand.group);
        for (AbstractCard card : handSnapshot) {
            transform(card);
        }
        this.player.hand.refreshHandLayout();
        this.isDone = true;
    }

    private void transform(AbstractCard original) {
        AbstractCard replacement = randomSameColorCard();
        if (replacement == null) {
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
