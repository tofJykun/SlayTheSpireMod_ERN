package general;

import basemod.abstracts.CustomSavable;
import cards.scholar.ParadiseLost;
import cards.scholar.WhiteNight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import java.util.ArrayList;

public final class WhiteNightProgress implements CustomSavable<Boolean> {
    public static final String SAVE_KEY = "ERN:WhiteNightCompleted";
    private static boolean completed;

    public static void resetRun() { completed = false; }

    public static boolean isUnavailable() {
        if (completed) return true;
        AbstractRoom room = CombatState.currentRoom();
        return room != null && !room.isBattleOver && CombatState.isInCombat();
    }

    public static void finishVictory() {
        AbstractRoom room = CombatState.currentRoom();
        if (room == null || room.smoked || AbstractDungeon.player == null
                || AbstractDungeon.player.isDead || AbstractDungeon.player.isDying) return;
        // Commit the ban before reward generation or any obtain/remove-card callbacks.
        completed = true;
        prunePools();
        AbstractCard reward = new ParadiseLost();
        UnlockTracker.markCardAsSeen(reward.cardID);
        CardHelper.obtain(reward.cardID, reward.rarity, reward.color);
        for (AbstractRelic relic : new ArrayList<>(AbstractDungeon.player.relics)) relic.onObtainCard(reward);
        AbstractDungeon.player.masterDeck.addToTop(reward);
        for (AbstractRelic relic : new ArrayList<>(AbstractDungeon.player.relics)) relic.onMasterDeckChange();
        for (AbstractCard card : new ArrayList<>(AbstractDungeon.player.masterDeck.group)) {
            if (WhiteNight.ID.equals(card.cardID)) AbstractDungeon.player.masterDeck.removeCard(card);
        }
        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(reward.makeStatEquivalentCopy()));
    }

    public static void prunePools() {
        for (CardGroup pool : new CardGroup[] {AbstractDungeon.commonCardPool, AbstractDungeon.uncommonCardPool,
                AbstractDungeon.rareCardPool, AbstractDungeon.colorlessCardPool, AbstractDungeon.curseCardPool,
                AbstractDungeon.srcCommonCardPool, AbstractDungeon.srcUncommonCardPool, AbstractDungeon.srcRareCardPool,
                AbstractDungeon.srcColorlessCardPool, AbstractDungeon.srcCurseCardPool}) {
            if (pool != null) pool.group.removeIf(card -> ParadiseLost.ID.equals(card.cardID)
                    || (completed && WhiteNight.ID.equals(card.cardID)));
        }
    }

    public static ArrayList<AbstractCard> candidates(ArrayList<AbstractCard> source) {
        ArrayList<AbstractCard> result = new ArrayList<>();
        for (AbstractCard card : source) if (!ExclusiveCardPool.isExcluded(card)) result.add(card);
        return result;
    }

    @Override
    public Boolean onSave() { return completed; }

    @Override
    public void onLoad(Boolean saved) {
        completed = Boolean.TRUE.equals(saved);
        prunePools();
    }
}
