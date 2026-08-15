package patches;

import actions.PlayScheduledCardAction;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayDeque;
import java.util.ArrayList;

public final class ScheduledUseCardPatch {
    private static final int MAX_TRIGGERED_PLAYS_PER_TURN = 999;
    private static final ArrayDeque<ArrayList<HandCardSnapshot>> HAND_SNAPSHOTS = new ArrayDeque<ArrayList<HandCardSnapshot>>();

    private ScheduledUseCardPatch() {
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "useCard",
            paramtypez = { AbstractCard.class, AbstractMonster.class, int.class })
    public static class UseCardPatch {
        @SpirePrefixPatch
        public static void prefix(AbstractPlayer __instance, AbstractCard playedCard,
                                  AbstractMonster target, int energyOnUse) {
            ArrayList<HandCardSnapshot> snapshot = new ArrayList<HandCardSnapshot>();
            if (__instance != null) {
                for (AbstractCard card : __instance.hand.group) {
                    if (card != playedCard && ScheduledField.isScheduled(card)
                            && !ScheduledField.isPendingAutoplay(card)) {
                        snapshot.add(new HandCardSnapshot(card, ScheduledField.getHandEntry(card)));
                    }
                }
            }
            HAND_SNAPSHOTS.push(snapshot);
        }

        @SpirePostfixPatch
        public static void postfix(AbstractPlayer __instance, AbstractCard playedCard,
                                   AbstractMonster target, int energyOnUse) {
            ArrayList<HandCardSnapshot> handSnapshot = HAND_SNAPSHOTS.isEmpty()
                    ? new ArrayList<HandCardSnapshot>()
                    : HAND_SNAPSHOTS.pop();
            if (playedCard == null || AbstractDungeon.player == null || AbstractDungeon.actionManager == null
                    || AbstractDungeon.actionManager.cardsPlayedThisTurn.size() > MAX_TRIGGERED_PLAYS_PER_TURN) {
                return;
            }

            for (HandCardSnapshot snapshot : handSnapshot) {
                AbstractCard card = snapshot.card;
                if (!AbstractDungeon.player.hand.contains(card)
                        || ScheduledField.getHandEntry(card) != snapshot.handEntry
                        || ScheduledField.isPendingAutoplay(card)) {
                    continue;
                }
                ScheduledField.setScheduled(card, ScheduledField.getScheduled(card) - 1);
                if (ScheduledField.getScheduled(card) == 0) {
                    ScheduledField.setPendingAutoplay(card, true);
                    AbstractDungeon.actionManager.addToBottom(new PlayScheduledCardAction(card));
                }
            }
        }
    }

    private static final class HandCardSnapshot {
        private final AbstractCard card;
        private final int handEntry;

        private HandCardSnapshot(AbstractCard card, int handEntry) {
            this.card = card;
            this.handEntry = handEntry;
        }
    }
}
