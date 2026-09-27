package patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import general.ExtraCardRewards;
import general.FragrantBranchOfYoreRewards;
import general.RelicRewardHelper;
import powers.RustedGoldCoinPower;
import powers.SilverPickledFowlFootPower;
import powers.DowsingRodPower;
import powers.PerfectScorePower;
import relics.CovetousGoldSerpentRing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class FragrantBranchOfYoreRewardPatch {
    @SpirePatch(clz = CombatRewardScreen.class, method = "setupItemReward")
    public static class SetupItemRewardPatch {
        @SpirePostfixPatch
        public static void postfix(CombatRewardScreen __instance) {
            AbstractRoom room = AbstractDungeon.getCurrRoom();
            int count = FragrantBranchOfYoreRewards.consume() + ExtraCardRewards.consume();
            if (count <= 0 || !(room instanceof MonsterRoom) || AbstractDungeon.player == null) {
                if (!(room instanceof MonsterRoom) || AbstractDungeon.player == null) {
                    return;
                }
                count = 0;
            }
            if (shouldAddCovetousGoldReward()) {
                count++;
            }
            if (shouldAddSilverPickledReward()) {
                count++;
            }
            int rareRewardCount = shouldAddDowsingRodRareReward() ? 1 : 0;
            boolean perfectScoreReward = shouldAddPerfectScoreReward(__instance, room);
            boolean addedRelicReward = false;
            if (shouldAddRustedGoldCoinRelicReward(room)) {
                __instance.rewards.add(new RewardItem(RelicRewardHelper.returnRandomEliteDropRelic()));
                addedRelicReward = true;
            }
            if (count <= 0 && rareRewardCount <= 0 && !perfectScoreReward) {
                if (addedRelicReward) {
                    __instance.positionRewards();
                }
                return;
            }

            boolean bossReward = room instanceof MonsterRoomBoss;
            for (int i = 0; i < count; i++) {
                RewardItem reward = bossReward ? createRareCardReward() : new RewardItem();
                if (reward.cards != null && reward.cards.size() > 0) {
                    __instance.rewards.add(reward);
                }
            }
            for (int i = 0; i < rareRewardCount; i++) {
                RewardItem reward = createRareCardReward();
                if (reward.cards != null && reward.cards.size() > 0) {
                    __instance.rewards.add(reward);
                }
            }
            if (perfectScoreReward) {
                RewardItem reward = createPerfectScoreReward();
                if (reward.cards != null && reward.cards.size() > 0) {
                    __instance.rewards.add(reward);
                }
            }
            __instance.positionRewards();
        }
    }

    private static boolean shouldAddCovetousGoldReward() {
        if (AbstractDungeon.player == null || !AbstractDungeon.player.hasRelic(CovetousGoldSerpentRing.ID)) {
            return false;
        }
        if (AbstractDungeon.cardRandomRng.random(99) >= CovetousGoldSerpentRing.CHANCE) {
            return false;
        }
        AbstractRelic relic = AbstractDungeon.player.getRelic(CovetousGoldSerpentRing.ID);
        if (relic != null) {
            relic.flash();
        }
        return true;
    }

    private static boolean shouldAddSilverPickledReward() {
        if (AbstractDungeon.player == null || !AbstractDungeon.player.hasPower(SilverPickledFowlFootPower.POWER_ID)) {
            return false;
        }
        AbstractPower power = AbstractDungeon.player.getPower(SilverPickledFowlFootPower.POWER_ID);
        int chance = Math.max(0, Math.min(100, power.amount));
        if (chance <= 0 || AbstractDungeon.cardRandomRng.random(99) >= chance) {
            return false;
        }
        power.flash();
        return true;
    }

    private static boolean shouldAddDowsingRodRareReward() {
        if (AbstractDungeon.player == null || !AbstractDungeon.player.hasPower(DowsingRodPower.POWER_ID)) {
            return false;
        }
        AbstractPower power = AbstractDungeon.player.getPower(DowsingRodPower.POWER_ID);
        int chance = Math.max(0, Math.min(100, power.amount));
        if (chance <= 0 || AbstractDungeon.cardRandomRng.random(99) >= chance) {
            return false;
        }
        power.flash();
        return true;
    }

    private static boolean shouldAddPerfectScoreReward(CombatRewardScreen screen, AbstractRoom room) {
        if (AbstractDungeon.player == null || !(room instanceof MonsterRoom)
                || !AbstractDungeon.player.hasPower(PerfectScorePower.POWER_ID)) {
            return false;
        }
        for (RewardItem reward : screen.rewards) {
            if (reward.type == RewardItem.RewardType.CARD) {
                AbstractPower power = AbstractDungeon.player.getPower(PerfectScorePower.POWER_ID);
                if (power != null) {
                    power.flash();
                }
                return true;
            }
        }
        return false;
    }

    private static boolean shouldAddRustedGoldCoinRelicReward(AbstractRoom room) {
        if (!isNormalMonsterRoom(room) || AbstractDungeon.player == null
                || !AbstractDungeon.player.hasPower(RustedGoldCoinPower.POWER_ID)) {
            return false;
        }
        AbstractPower power = AbstractDungeon.player.getPower(RustedGoldCoinPower.POWER_ID);
        int chance = Math.max(0, Math.min(100, power.amount));
        if (chance <= 0 || AbstractDungeon.cardRandomRng.random(99) >= chance) {
            return false;
        }
        power.flash();
        return true;
    }

    private static boolean isNormalMonsterRoom(AbstractRoom room) {
        return room instanceof MonsterRoom
                && !(room instanceof MonsterRoomElite)
                && !(room instanceof MonsterRoomBoss);
    }

    private static RewardItem createRareCardReward() {
        RewardItem reward = new RewardItem(0);
        reward.type = RewardItem.RewardType.CARD;
        reward.cards = getRareRewardCards();
        reward.text = RewardItem.TEXT[2];
        ReflectionHacks.setPrivate(reward, RewardItem.class, "isBoss", true);
        return reward;
    }

    private static RewardItem createPerfectScoreReward() {
        RewardItem reward = new RewardItem();
        if (reward.cards == null) {
            return reward;
        }
        for (AbstractCard card : reward.cards) {
            if (card.canUpgrade()) {
                card.upgrade();
            }
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                relic.onPreviewObtainCard(card);
            }
        }
        return reward;
    }

    private static ArrayList<AbstractCard> getRareRewardCards() {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        int numCards = 3;
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            numCards = relic.changeNumberOfCardsInReward(numCards);
        }
        if (ModHelper.isModEnabled("Binary")) {
            numCards--;
        }
        if (numCards <= 0) {
            return cards;
        }

        ArrayList<AbstractCard> pool = getRarePool();
        if (pool.isEmpty()) {
            return cards;
        }

        HashSet<String> used = new HashSet<>();
        for (int i = 0; i < numCards; i++) {
            AbstractCard card = getRandomRare(pool, used);
            if (card == null) {
                break;
            }
            AbstractCard copy = card.makeCopy();
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                relic.onPreviewObtainCard(copy);
            }
            cards.add(copy);
            used.add(copy.cardID);
        }
        return cards;
    }

    private static AbstractCard getRandomRare(ArrayList<AbstractCard> pool, HashSet<String> used) {
        ArrayList<AbstractCard> available = new ArrayList<>();
        for (AbstractCard card : pool) {
            if (!used.contains(card.cardID)) {
                available.add(card);
            }
        }
        ArrayList<AbstractCard> source = available.isEmpty() ? pool : available;
        return source.get(AbstractDungeon.cardRandomRng.random(source.size() - 1));
    }

    private static ArrayList<AbstractCard> getRarePool() {
        if (AbstractDungeon.player.hasRelic("PrismaticShard")) {
            return getAnyColorRarePool();
        }
        return new ArrayList<>(AbstractDungeon.rareCardPool.group);
    }

    private static ArrayList<AbstractCard> getAnyColorRarePool() {
        ArrayList<AbstractCard> pool = new ArrayList<>();
        for (Map.Entry<String, AbstractCard> entry : CardLibrary.cards.entrySet()) {
            AbstractCard card = entry.getValue();
            if (card.rarity == AbstractCard.CardRarity.RARE
                    && card.type != AbstractCard.CardType.CURSE
                    && card.type != AbstractCard.CardType.STATUS
                    && (!UnlockTracker.isCardLocked(entry.getKey()) || Settings.treatEverythingAsUnlocked())) {
                pool.add(card);
            }
        }
        return pool;
    }
}
