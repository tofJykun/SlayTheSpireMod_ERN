package patches;

import actions.RetaliatoryVigorRemovalAction;
import cards.guardian.GolemHalberd;
import general.SmithingBody;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import powers.RetaliatoryPower;

@SpirePatch(clz = VigorPower.class, method = "onUseCard")
public class RetaliatoryPatch {
    @SpirePrefixPatch
    public static SpireReturn<Void> prefix(VigorPower __instance, AbstractCard card, UseCardAction action) {
        relics.SunlightMaggot.onVigorUsed(__instance.owner, card, __instance.amount);
        boolean refundByCard = SmithingBody.behavior(card) instanceof GolemHalberd;
        if (card != null && card.type == AbstractCard.CardType.ATTACK
                && __instance.owner != null
                && (refundByCard || __instance.owner.hasPower(RetaliatoryPower.POWER_ID))) {
            __instance.flash();
            AbstractDungeon.actionManager.addToBottom(new RetaliatoryVigorRemovalAction(__instance.owner, refundByCard));
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}
