package general;

import cards.scholar.Empyrean;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import java.util.ArrayList;

public final class EmpyreanObtain {
    private EmpyreanObtain() { }

    public static boolean blocks(CardGroup group, AbstractCard card) {
        if (AbstractDungeon.player == null || group != AbstractDungeon.player.masterDeck
                || card == null || !Empyrean.ID.equals(card.cardID)) return false;
        for (AbstractCard existing : group.group) {
            if (Empyrean.ID.equals(existing.cardID)) return true;
        }
        return false;
    }

    public static void obtain(boolean upgraded) {
        if (AbstractDungeon.player == null || AbstractDungeon.player.isDead) return;
        AbstractCard reward = new Empyrean();
        if (upgraded) reward.upgrade();
        if (blocks(AbstractDungeon.player.masterDeck, reward)) return;
        // Commit during resolution so simultaneous rewards cannot bypass the one-card limit.
        UnlockTracker.markCardAsSeen(reward.cardID);
        CardHelper.obtain(reward.cardID, reward.rarity, reward.color);
        for (AbstractRelic relic : new ArrayList<>(AbstractDungeon.player.relics)) relic.onObtainCard(reward);
        AbstractDungeon.player.masterDeck.addToTop(reward);
        for (AbstractRelic relic : new ArrayList<>(AbstractDungeon.player.relics)) relic.onMasterDeckChange();
        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(reward.makeStatEquivalentCopy()));
    }
}
