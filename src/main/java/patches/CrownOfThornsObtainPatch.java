package patches;

import cards.scholar.CrownOfThorns;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class CrownOfThornsObtainPatch {
    private static void onAdded(CardGroup group, AbstractCard card) {
        if (!(card instanceof CrownOfThorns) || CardCrawlGame.loadingSave) {
            return;
        }
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null || group != player.masterDeck || player.isDead) {
            return;
        }
        // Acquisitions also happen outside combat, where queued actions do not run.
        // Native damage handles Tungsten Rod, revival and lethal HP loss.
        player.damage(new DamageInfo(null, CrownOfThorns.HP_LOSS, DamageInfo.DamageType.HP_LOSS));
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
