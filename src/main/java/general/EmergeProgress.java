package general;

import basemod.abstracts.CustomSavable;
import basemod.interfaces.PostUpdateSubscriber;
import cards.scholar.Emerge;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public final class EmergeProgress implements CustomSavable<Boolean>, PostUpdateSubscriber {
    public static final String SAVE_KEY = "ERN:EmergeEarned";
    private static final int REQUIRED_TYPES = 30;
    private static boolean earned;

    public static void resetRun() { earned = false; }

    @Override
    public void receivePostUpdate() {
        if (earned || CardCrawlGame.mode != CardCrawlGame.GameMode.GAMEPLAY || CardCrawlGame.loadingSave
                || AbstractDungeon.player == null || AbstractDungeon.player.isDead
                || AbstractDungeon.player.isDying || CombatState.currentRoom() == null
                || AbstractDungeon.player.masterDeck.size() < REQUIRED_TYPES) return;
        Set<String> types = new HashSet<>();
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) types.add(card.cardID);
        if (types.size() < REQUIRED_TYPES) return;
        // Set the run flag before obtain callbacks; commit the reward before the next save.
        earned = true;
        AbstractCard reward = new Emerge();
        UnlockTracker.markCardAsSeen(reward.cardID);
        CardHelper.obtain(reward.cardID, reward.rarity, reward.color);
        for (AbstractRelic relic : new ArrayList<>(AbstractDungeon.player.relics)) relic.onObtainCard(reward);
        AbstractDungeon.player.masterDeck.addToTop(reward);
        for (AbstractRelic relic : new ArrayList<>(AbstractDungeon.player.relics)) relic.onMasterDeckChange();
        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(reward.makeStatEquivalentCopy()));
    }

    @Override
    public Boolean onSave() { return earned; }

    @Override
    public void onLoad(Boolean saved) { earned = Boolean.TRUE.equals(saved); }
}
