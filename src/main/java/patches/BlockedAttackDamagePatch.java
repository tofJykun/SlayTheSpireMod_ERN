package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Map;
import java.util.WeakHashMap;

public class BlockedAttackDamagePatch {
    private static final Map<DamageInfo, Integer> BLOCKED_DAMAGE = new WeakHashMap<>();
    private static final Map<DamageInfo, Integer> INCOMING_DAMAGE = new WeakHashMap<>();

    public static int getBlockedDamage(DamageInfo info) {
        Integer amount = BLOCKED_DAMAGE.get(info);
        return amount == null ? 0 : amount;
    }

    @SpirePatch(clz = AbstractCreature.class, method = "decrementBlock",
            paramtypez = { DamageInfo.class, int.class })
    public static class RecordBlockedDamage {
        @SpirePrefixPatch
        public static void prefix(AbstractCreature __instance, DamageInfo info, int damageAmount) {
            if (info == null) return;
            BLOCKED_DAMAGE.remove(info);
            INCOMING_DAMAGE.remove(info);
            if (((__instance instanceof AbstractPlayer && info.owner instanceof AbstractMonster)
                        || (__instance instanceof AbstractMonster && info.owner instanceof AbstractPlayer))
                    && info.type == DamageInfo.DamageType.NORMAL
                    && damageAmount > 0) {
                // decrementBlock mutates damageAmount, so its postfix cannot read the original input.
                INCOMING_DAMAGE.put(info, damageAmount);
            }
        }

        @SpirePostfixPatch
        public static int postfix(int __result, AbstractCreature __instance, DamageInfo info) {
            Integer incoming = INCOMING_DAMAGE.remove(info);
            if (incoming != null) {
                BLOCKED_DAMAGE.put(info, Math.max(0, incoming - Math.max(0, __result)));
            }
            return __result;
        }
    }
}
