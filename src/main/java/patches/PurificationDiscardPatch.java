package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import general.PurificationDiscardTracker;

public final class PurificationDiscardPatch {
    @SpirePatch(clz = CardGroup.class, method = "moveToDiscardPile", paramtypez = {AbstractCard.class})
    public static class RecordHandDiscardPatch {
        @SpirePostfixPatch
        public static void postfix(CardGroup __instance, AbstractCard card) {
            if (AbstractDungeon.player != null && __instance == AbstractDungeon.player.hand) {
                PurificationDiscardTracker.record(card);
            }
        }
    }

    private PurificationDiscardPatch() {
    }
}
