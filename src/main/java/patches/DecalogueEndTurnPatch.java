package patches;

import actions.DecalogueRetainAction;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import relics.Decalogue;

public final class DecalogueEndTurnPatch {
    private DecalogueEndTurnPatch() {
    }

    @SpirePatch(clz = AbstractRoom.class, method = "endTurn")
    public static class BeforeEndTurnDiscardPatch {
        @SpirePrefixPatch
        public static void prefix(AbstractRoom __instance) {
            if (AbstractDungeon.player == null || AbstractDungeon.actionManager == null
                    || !AbstractDungeon.player.hasRelic(Decalogue.ID)
                    || AbstractDungeon.actionManager.cardsPlayedThisTurn.size() > 1
                    || AbstractDungeon.player.hand.isEmpty()) {
                return;
            }
            AbstractDungeon.player.getRelic(Decalogue.ID).flash();
            AbstractDungeon.actionManager.addToBottom(new DecalogueRetainAction());
        }
    }
}
