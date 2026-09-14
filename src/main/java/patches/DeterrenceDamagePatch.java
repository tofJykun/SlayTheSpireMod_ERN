package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import powers.DeterrencePower;

public class DeterrenceDamagePatch {
    @SpirePatch(clz = DamageInfo.class, method = "applyPowers",
            paramtypez = { AbstractCreature.class, AbstractCreature.class })
    public static class ApplyPowersPatch {
        @SpirePostfixPatch
        public static void postfix(DamageInfo __instance, AbstractCreature owner, AbstractCreature target) {
            DeterrencePower.modifyDamage(__instance, owner, target);
        }
    }
}
