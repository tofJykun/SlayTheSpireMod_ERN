package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Map;
import java.util.WeakHashMap;

public class BlockedAttackDamagePatch {
    private static final Map<DamageInfo, Integer> BLOCKED_DAMAGE = new WeakHashMap<>();

    public static int getBlockedDamage(DamageInfo info) {
        Integer amount = BLOCKED_DAMAGE.get(info);
        return amount == null ? 0 : amount;
    }

    @SpirePatch(clz = AbstractCreature.class, method = "decrementBlock",
            paramtypez = { DamageInfo.class, int.class })
    public static class RecordBlockedDamage {
        @SpirePostfixPatch
        public static int postfix(int __result, AbstractCreature __instance, DamageInfo info, int damageAmount) {
            if (__instance instanceof AbstractPlayer
                    && info != null
                    && info.owner instanceof AbstractMonster
                    && info.type == DamageInfo.DamageType.NORMAL
                    && damageAmount > 0) {
                BLOCKED_DAMAGE.put(info, Math.max(0, damageAmount - __result));
            } else if (info != null) {
                BLOCKED_DAMAGE.remove(info);
            }
            return __result;
        }
    }
}
