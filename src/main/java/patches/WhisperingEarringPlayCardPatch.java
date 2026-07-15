package patches;

import actions.RandomPlayHelper;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import relics.WhisperingEarring;

public class WhisperingEarringPlayCardPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "playCard")
    public static class PlayCardPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> prefix(AbstractPlayer __instance) {
            AbstractCard card = __instance.hoveredCard;
            WhisperingEarring relic = getRelic();
            if (card == null || relic == null || !relic.shouldRandomize(card) || queueContains(card)) {
                return SpireReturn.Continue();
            }

            InputHelper.justClickedLeft = false;
            ReflectionHacks.setPrivate(__instance, AbstractPlayer.class, "hoverEnemyWaitTimer", 1.0F);
            card.unhover();

            if (RandomPlayHelper.shouldChooseRandomPlay(__instance)) {
                AbstractMonster target = ReflectionHacks.getPrivate(__instance, AbstractPlayer.class, "hoveredMonster");
                if (target == null) {
                    return SpireReturn.Continue();
                }
                relic.consumeInsightChoice();
                relic.consume(card);
                AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(card, target));
            } else {
                relic.consume(card);
                AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(card, true,
                        EnergyPanel.getCurrentEnergy(), false, false));
            }

            ReflectionHacks.setPrivate(__instance, AbstractPlayer.class, "isUsingClickDragControl", false);
            __instance.hoveredCard = null;
            __instance.isDraggingCard = false;
            return SpireReturn.Return(null);
        }

        private static WhisperingEarring getRelic() {
            if (AbstractDungeon.player == null || !AbstractDungeon.player.hasRelic(WhisperingEarring.ID)) {
                return null;
            }
            return (WhisperingEarring)AbstractDungeon.player.getRelic(WhisperingEarring.ID);
        }

        private static boolean queueContains(AbstractCard card) {
            for (CardQueueItem item : AbstractDungeon.actionManager.cardQueue) {
                if (item.card == card) {
                    return true;
                }
            }
            return false;
        }
    }
}
