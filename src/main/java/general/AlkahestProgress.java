package general;

import basemod.abstracts.CustomSavable;
import basemod.interfaces.PostUpdateSubscriber;
import cards.scholar.Alkahest;
import cards.scholar.Salt;
import cards.scholar.Telescope;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import relics.Lordvessel;
import java.util.ArrayList;

public final class AlkahestProgress implements CustomSavable<Boolean>, PostUpdateSubscriber {
    public static final String SAVE_KEY = "ERN:AlkahestEarned";
    private static boolean earned;

    public static void resetRun() { earned = false; }

    @Override
    public void receivePostUpdate() {
        if (earned || CardCrawlGame.mode != CardCrawlGame.GameMode.GAMEPLAY || CardCrawlGame.loadingSave
                || AbstractDungeon.player == null || AbstractDungeon.player.isDead
                || AbstractDungeon.player.isDying || CombatState.currentRoom() == null
                || !AbstractDungeon.player.hasRelic(Lordvessel.ID)) return;
        boolean telescope = false;
        boolean salt = false;
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (Telescope.ID.equals(card.cardID)) telescope = true;
            if (Salt.ID.equals(card.cardID)) salt = true;
        }
        if (!telescope || !salt) return;
        earned = true;
        AbstractCard reward = new Alkahest();
        UnlockTracker.markCardAsSeen(reward.cardID);
        CardHelper.obtain(reward.cardID, reward.rarity, reward.color);
        for (AbstractRelic relic : new ArrayList<>(AbstractDungeon.player.relics)) relic.onObtainCard(reward);
        // Commit the card and save flag together instead of waiting for the animation to finish.
        AbstractDungeon.player.masterDeck.addToTop(reward);
        for (AbstractRelic relic : new ArrayList<>(AbstractDungeon.player.relics)) relic.onMasterDeckChange();
        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(reward.makeStatEquivalentCopy()));
    }

    @Override
    public Boolean onSave() { return earned; }

    @Override
    public void onLoad(Boolean saved) { earned = Boolean.TRUE.equals(saved); }
}
