package patches;

import cards.scholar.CrownOfThorns;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class CrownOfThornsObtainPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
    public static class Pending {
        public static final SpireField<Integer> obtains = new SpireField<>(() -> 0);
    }

    private static void onAdded(CardGroup group, AbstractCard card) {
        if (!(card instanceof CrownOfThorns) || CardCrawlGame.loadingSave) {
            return;
        }
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null || group != player.masterDeck || player.isDead) {
            return;
        }
        // Obtain effects run inside effectList's iterator; damage adds effects to that list.
        Pending.obtains.set(player, Pending.obtains.get(player) + 1);
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "update")
    public static class AfterUpdate {
        @SpirePostfixPatch
        public static void postfix() {
            AbstractPlayer player = AbstractDungeon.player;
            if (player == null) return;
            int obtains = Pending.obtains.get(player);
            Pending.obtains.set(player, 0);
            if (CardCrawlGame.loadingSave) return;
            // Outside both effect iterators, also runs outside combat unlike queued actions.
            for (int i = 0; i < obtains && !player.isDead; i++) {
                player.damage(new DamageInfo(null, CrownOfThorns.HP_LOSS, DamageInfo.DamageType.HP_LOSS));
            }
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "addToTop", paramtypez = {AbstractCard.class})
    public static class Top {
        @SpirePostfixPatch
        public static void postfix(CardGroup __instance, AbstractCard c) {
            onAdded(__instance, c);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "addToBottom", paramtypez = {AbstractCard.class})
    public static class Bottom {
        @SpirePostfixPatch
        public static void postfix(CardGroup __instance, AbstractCard c) {
            onAdded(__instance, c);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "addToRandomSpot", paramtypez = {AbstractCard.class})
    public static class RandomSpot {
        @SpirePostfixPatch
        public static void postfix(CardGroup __instance, AbstractCard c) {
            onAdded(__instance, c);
        }
    }
}
