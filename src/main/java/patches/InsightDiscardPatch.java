package patches;

import actions.RandomPlayHelper;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class InsightDiscardPatch {
    private static final Set<DiscardAction> INSIGHT_REWRITTEN =
            Collections.newSetFromMap(new WeakHashMap<DiscardAction, Boolean>());
    private static final Map<DiscardAction, Integer> RANDOM_DISCARD_HAND_SIZE =
            new WeakHashMap<DiscardAction, Integer>();

    @SpirePatch(clz = DiscardAction.class, method = "update")
    public static class RandomDiscardToChoicePatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> prefix(DiscardAction __instance) {
            Float duration = ReflectionHacks.getPrivateInherited(__instance, DiscardAction.class, "duration");
            if (duration == null || duration.floatValue() != Settings.ACTION_DUR_XFAST) {
                return SpireReturn.Continue();
            }
            Boolean isRandom = ReflectionHacks.getPrivate(__instance, DiscardAction.class, "isRandom");
            if (isRandom == null || !isRandom.booleanValue()) {
                return SpireReturn.Continue();
            }
            AbstractPlayer player = ReflectionHacks.getPrivate(__instance, DiscardAction.class, "p");
            if (player == null
                    || player.hand == null
                    || player.hand.size() <= __instance.amount
                    || !RandomPlayHelper.shouldChooseRandomDiscard(player)) {
                return SpireReturn.Continue();
            }

            DiscardAction.numDiscarded = __instance.amount;
            INSIGHT_REWRITTEN.add(__instance);
            AbstractDungeon.handCardSelectScreen.open(RandomPlayHelper.consumeInsightDiscardPrompt(player),
                    __instance.amount, false);
            player.hand.applyPowers();
            AbstractDungeon.actionManager.addToTop(new RetrieveInsightDiscardSelectionAction(__instance, player));
            __instance.isDone = true;
            return SpireReturn.Return(null);
        }
    }

    private static class RetrieveInsightDiscardSelectionAction extends AbstractGameAction {
        private final DiscardAction originalAction;
        private final AbstractPlayer player;

        private RetrieveInsightDiscardSelectionAction(DiscardAction originalAction, AbstractPlayer player) {
            this.originalAction = originalAction;
            this.player = player;
            this.duration = Settings.ACTION_DUR_XFAST;
        }

        @Override
        public void update() {
            if (this.duration == Settings.ACTION_DUR_XFAST) {
                tickDuration();
                return;
            }
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                Boolean endTurn = ReflectionHacks.getPrivate(this.originalAction, DiscardAction.class, "endTurn");
                boolean isEndTurn = endTurn != null && endTurn.booleanValue();
                for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    this.player.hand.moveToDiscardPile(card);
                    RandomPlayHelper.notifyRandomCardDiscarded();
                    if (!isEndTurn) {
                        card.triggerOnManualDiscard();
                    }
                    GameActionManager.incrementDiscard(isEndTurn);
                }
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
                this.player.hand.applyPowers();
            }
            tickDuration();
        }
    }

    @SpirePatch(clz = DiscardAction.class, method = "update")
    public static class RandomDiscardBoonPatch {
        @SpirePrefixPatch
        public static void prefix(DiscardAction __instance) {
            Boolean isRandom = ReflectionHacks.getPrivate(__instance, DiscardAction.class, "isRandom");
            AbstractPlayer player = ReflectionHacks.getPrivate(__instance, DiscardAction.class, "p");
            if (Boolean.TRUE.equals(isRandom) && player != null && !RANDOM_DISCARD_HAND_SIZE.containsKey(__instance)) {
                RANDOM_DISCARD_HAND_SIZE.put(__instance, player.hand.size());
            }
        }

        @SpirePostfixPatch
        public static void postfix(DiscardAction __instance) {
            Integer initialSize = RANDOM_DISCARD_HAND_SIZE.get(__instance);
            if (!__instance.isDone || initialSize == null) {
                return;
            }
            RANDOM_DISCARD_HAND_SIZE.remove(__instance);
            if (INSIGHT_REWRITTEN.remove(__instance)) {
                return;
            }
            AbstractPlayer player = ReflectionHacks.getPrivate(__instance, DiscardAction.class, "p");
            if (player == null) {
                return;
            }
            int discarded = Math.max(0, initialSize - player.hand.size());
            for (int i = 0; i < discarded; i++) {
                RandomPlayHelper.notifyRandomCardDiscarded();
            }
        }
    }
}
