package patches;

import cards.tempcards.LotteryTicket;
import basemod.helpers.dynamicvariables.MagicNumberVariable;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import relics.AnotherBottle;

public class AnotherBottlePatch {
    public static void refresh(AbstractCard card, AbstractPlayer player) {
        if (!(card instanceof LotteryTicket)) return;
        int bonus = player != null && player.hasRelic(AnotherBottle.ID) ? AnotherBottle.TOKEN_BONUS : 0;
        card.magicNumber = card.baseMagicNumber + bonus;
        card.isMagicNumberModified = bonus != 0;
    }

    private static void refreshPreview(AbstractCard card) {
        if (!(card instanceof LotteryTicket)) return;
        // Card construction/upgrades can run before AbstractDungeon is safe to initialize.
        refresh(card, CardCrawlGame.dungeon == null ? null : AbstractDungeon.player);
    }

    @SpirePatch(clz = MagicNumberVariable.class, method = "baseValue")
    public static class DisplayBaseValue {
        @SpirePostfixPatch
        public static int postfix(int __result, MagicNumberVariable __instance, AbstractCard card) {
            if (!(card instanceof LotteryTicket)) return __result;
            // BaseMod's English enlarged preview reads baseValue, not value.
            refreshPreview(card);
            return card.magicNumber;
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "applyPowers")
    public static class ApplyPowers {
        @SpirePostfixPatch
        public static void postfix(AbstractCard __instance) {
            refreshPreview(__instance);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "render", paramtypez = {SpriteBatch.class})
    public static class Render {
        @SpirePrefixPatch
        public static void prefix(AbstractCard __instance, SpriteBatch sb) {
            refreshPreview(__instance);
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderDescription")
    public static class PopupEnglish {
        @SpirePrefixPatch
        public static void prefix(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card) {
            refreshPreview(___card);
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderDescriptionCN")
    public static class PopupChinese {
        @SpirePrefixPatch
        public static void prefix(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card) {
            refreshPreview(___card);
        }
    }

    @SpirePatch(clz = LotteryTicket.class, method = "upgrade")
    public static class Upgrade {
        @SpirePostfixPatch
        public static void postfix(LotteryTicket __instance) {
            refreshPreview(__instance);
        }
    }

    @SpirePatch(clz = LotteryTicket.class, method = "use")
    public static class Use {
        @SpirePrefixPatch
        public static void prefix(LotteryTicket __instance, AbstractPlayer player, AbstractMonster monster) {
            refresh(__instance, player);
        }
    }
}
