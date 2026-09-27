package patches;

import cards.undertaker.ImpenetrableThorns;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import general.ExtraCardRewards;

public class ImpenetrableThornsExhaustPatch {
    @SpirePatch(clz = CardGroup.class, method = "moveToExhaustPile", paramtypez = {AbstractCard.class})
    public static class ExhaustPatch {
        @SpirePostfixPatch
        public static void postfix(CardGroup __instance, AbstractCard card) {
            if (card instanceof ImpenetrableThorns && AbstractDungeon.player != null
                    && AbstractDungeon.player.exhaustPile.contains(card)) {
                ExtraCardRewards.add(1);
            }
        }
    }
}
