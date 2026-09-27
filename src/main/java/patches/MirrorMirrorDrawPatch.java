package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import relics.MirrorMirror;

public final class MirrorMirrorDrawPatch {
    private static boolean drawing;

    private MirrorMirrorDrawPatch() {
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "draw", paramtypez = {int.class})
    public static class DrawPatch {
        @SpirePrefixPatch
        public static void prefix(AbstractPlayer __instance) {
            drawing = __instance != null && __instance.hasRelic(MirrorMirror.ID);
        }

        @SpirePostfixPatch
        public static void postfix(AbstractPlayer __instance) {
            drawing = false;
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "addToHand", paramtypez = {AbstractCard.class})
    public static class AddToHandPatch {
        @SpirePostfixPatch
        public static void postfix(CardGroup __instance, AbstractCard card) {
            if (!drawing || __instance == null || __instance.type != CardGroup.CardGroupType.HAND || card == null) {
                return;
            }
            __instance.group.remove(card);
            __instance.group.add(0, card);
        }
    }
}
