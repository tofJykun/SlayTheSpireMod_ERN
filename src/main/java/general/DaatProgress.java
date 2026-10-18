package general;

import basemod.abstracts.CustomSavable;
import cards.scholar.Daat;
import cards.scholar.Sephirot;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import java.util.ArrayList;

public final class DaatProgress implements CustomSavable<Boolean> {
    public static final String SAVE_KEY = "ERN:DaatEarned";
    private static boolean earned;

    public static void resetRun() { earned = false; }

    /** Called only after Fragile has removed its linked permanent original. */
    public static void onFragileRemoval(AbstractCard master) {
        if (earned || master == null || !Sephirot.ID.equals(master.cardID)
                || AbstractDungeon.player == null || AbstractDungeon.player.masterDeck.contains(master)) return;
        earned = true;
        AbstractCard reward = new Daat();
        UnlockTracker.markCardAsSeen(reward.cardID);
        CardHelper.obtain(reward.cardID, reward.rarity, reward.color);
        for (AbstractRelic relic : new ArrayList<>(AbstractDungeon.player.relics)) relic.onObtainCard(reward);
        AbstractDungeon.player.masterDeck.addToTop(reward);
        for (AbstractRelic relic : new ArrayList<>(AbstractDungeon.player.relics)) relic.onMasterDeckChange();
        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(reward.makeStatEquivalentCopy()));
    }

    @Override public Boolean onSave() { return earned; }
    @Override public void onLoad(Boolean saved) { earned = Boolean.TRUE.equals(saved); }
}
