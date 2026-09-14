package patches;

import actions.OneTwoPunchReturnAction;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class OneTwoPunchPatch {
    @SpirePatch(clz = UseCardAction.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static final SpireField<Boolean> returnAfterUse = new SpireField<>(() -> false);
    }

    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class AfterUsePatch {
        @SpirePostfixPatch
        public static void postfix(UseCardAction __instance) {
            if (!__instance.isDone || !Fields.returnAfterUse.get(__instance)) {
                return;
            }
            Fields.returnAfterUse.set(__instance, false);
            AbstractCard card = ReflectionHacks.getPrivate(__instance, UseCardAction.class, "targetCard");
            if (AbstractDungeon.player != null && card != null && !card.purgeOnUse) {
                // Let exhaust hooks and post-use forging finish before retrieving the original.
                AbstractDungeon.actionManager.addToBottom(
                        new OneTwoPunchReturnAction(AbstractDungeon.player, card));
            }
        }
    }
}
