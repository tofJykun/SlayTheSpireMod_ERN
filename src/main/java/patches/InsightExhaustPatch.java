package patches;

import actions.RandomPlayHelper;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.LaplaceDemon;

public class InsightExhaustPatch {
    @SpirePatch(clz = ExhaustAction.class, method = "update")
    public static class RandomExhaustToChoicePatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> prefix(ExhaustAction __instance) {
            Float duration = ReflectionHacks.getPrivateInherited(__instance, ExhaustAction.class, "duration");
            Float startDuration = ReflectionHacks.getPrivateInherited(__instance, ExhaustAction.class,
                    "startDuration");
            if (duration == null || startDuration == null || duration.floatValue() != startDuration.floatValue()) {
                return SpireReturn.Continue();
            }
            Boolean isRandom = ReflectionHacks.getPrivate(__instance, ExhaustAction.class, "isRandom");
            if (isRandom == null || !isRandom.booleanValue()) {
                return SpireReturn.Continue();
            }
            AbstractPlayer player = ReflectionHacks.getPrivate(__instance, ExhaustAction.class, "p");
            AbstractRelic laplaceDemon = player == null ? null : player.getRelic(LaplaceDemon.ID);
            boolean shouldUseLaplaceDemon = laplaceDemon instanceof LaplaceDemon;
            if (player == null
                    || player.hand == null
                    || player.hand.size() <= __instance.amount
                    || (!shouldUseLaplaceDemon && !RandomPlayHelper.shouldChooseRandomExhaust(player))) {
                return SpireReturn.Continue();
            }

            Boolean anyNumber = ReflectionHacks.getPrivate(__instance, ExhaustAction.class, "anyNumber");
            Boolean canPickZero = ReflectionHacks.getPrivate(__instance, ExhaustAction.class, "canPickZero");
            String prompt;
            if (shouldUseLaplaceDemon) {
                laplaceDemon.flash();
                prompt = ((LaplaceDemon)laplaceDemon).getExhaustPrompt();
            } else {
                prompt = RandomPlayHelper.consumeInsightExhaustPrompt(player);
            }
            ExhaustAction.numExhausted = __instance.amount;
            AbstractDungeon.handCardSelectScreen.open(prompt,
                    __instance.amount,
                    anyNumber != null && anyNumber.booleanValue(),
                    canPickZero != null && canPickZero.booleanValue());
            AbstractDungeon.actionManager.addToTop(new RetrieveInsightExhaustSelectionAction(player));
            __instance.isDone = true;
            return SpireReturn.Return(null);
        }
    }

    private static class RetrieveInsightExhaustSelectionAction extends AbstractGameAction {
        private final AbstractPlayer player;

        private RetrieveInsightExhaustSelectionAction(AbstractPlayer player) {
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
                for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    this.player.hand.moveToExhaustPile(card);
                }
                if (CardCrawlGame.dungeon != null) {
                    CardCrawlGame.dungeon.checkForPactAchievement();
                }
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            }
            this.isDone = true;
        }
    }
}
