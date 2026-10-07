package patches;

import actions.AlphaToOmegaSequencer;
import actions.AvelynSequencer;
import actions.PackagingSequencer;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.PlayedCardHistory;

public class PlayedCardHistoryPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "useCard",
            paramtypez = {AbstractCard.class, AbstractMonster.class, int.class})
    public static class BeforeUse {
        @SpirePrefixPatch
        public static void prefix(AbstractPlayer __instance, AbstractCard card, AbstractMonster monster, int energyOnUse) {
            PlayedCardHistory.record(card);
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "clear")
    public static class Reset {
        @SpirePostfixPatch
        public static void postfix(GameActionManager __instance) {
            PlayedCardHistory.clear();
            AvelynSequencer.clear();
            AlphaToOmegaSequencer.clear();
            PackagingSequencer.clear();
        }
    }
}
