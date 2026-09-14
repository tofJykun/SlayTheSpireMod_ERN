package patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import powers.StoneRingPower;
import relics.OldGreataxe;

public class OldGreataxeDamagePatch {
    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class RecordTargetPatch {
        @SpirePrefixPatch
        public static void prefix(AbstractMonster __instance, DamageInfo info) {
            OldGreataxe.recordAttackedEnemy(__instance, info);
            StoneRingPower.recordAttackedEnemy(__instance, info);
        }
    }

    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class FinishTrackingPatch {
        @SpirePostfixPatch
        public static void postfix(UseCardAction __instance) {
            if (!__instance.isDone) {
                return;
            }
            AbstractCard card = ReflectionHacks.getPrivate(__instance, UseCardAction.class, "targetCard");
            OldGreataxe.queueFinish(card);
            StoneRingPower.queueFinish(card);
        }
    }
}
