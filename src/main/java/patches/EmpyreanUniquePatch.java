package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import general.EmpyreanObtain;

public final class EmpyreanUniquePatch {
    @SpirePatch(clz = CardGroup.class, method = "addToTop")
    @SpirePatch(clz = CardGroup.class, method = "addToBottom")
    @SpirePatch(clz = CardGroup.class, method = "addToRandomSpot")
    @SpirePatch(clz = CardGroup.class, method = "addToHand")
    public static class Add {
        @SpirePrefixPatch public static SpireReturn<Void> prefix(CardGroup __instance, AbstractCard c) {
            return EmpyreanObtain.blocks(__instance, c) ? SpireReturn.Return(null) : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ShowCardAndObtainEffect.class, method = "update")
    @SpirePatch(clz = FastCardObtainEffect.class, method = "update")
    public static class ObtainEffect {
        @SpirePrefixPatch public static SpireReturn<Void> prefix(AbstractGameEffect __instance, AbstractCard ___card) {
            if (AbstractDungeon.player != null && EmpyreanObtain.blocks(AbstractDungeon.player.masterDeck, ___card)) {
                __instance.isDone = true;
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
