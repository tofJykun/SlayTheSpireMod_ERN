package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * Removes stale custom-play references before the native shuffle animation
 * starts rendering the card from the draw pile.
 */
public final class ShuffleCardReferencePatch {
    private ShuffleCardReferencePatch() {
    }

    @SpirePatch(clz = SoulGroup.class, method = "shuffle",
            paramtypez = {AbstractCard.class, boolean.class})
    public static class RemoveStaleLimboReferencePatch {
        @SpirePrefixPatch
        public static void prefix(SoulGroup __instance, AbstractCard card, boolean isInvisible) {
            if (card == null || AbstractDungeon.player == null || AbstractDungeon.player.limbo == null) {
                return;
            }

            while (AbstractDungeon.player.limbo.group.remove(card)) {
                // A card must have only one render owner while it is shuffled.
            }
        }
    }
}
