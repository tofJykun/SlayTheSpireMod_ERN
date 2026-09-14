package patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import general.PackagingCard;

public class PackagingUseCardPatch {
    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class RefreshAfterUsePatch {
        @SpirePostfixPatch
        public static void postfix(UseCardAction __instance) {
            if (!__instance.isDone) {
                return;
            }
            AbstractCard card = ReflectionHacks.getPrivate(__instance, UseCardAction.class, "targetCard");
            if (card instanceof PackagingCard) {
                ((PackagingCard)card).refreshAfterPackagingUseAction();
            }
        }
    }
}
