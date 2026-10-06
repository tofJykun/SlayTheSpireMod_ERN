package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import powers.HoverPower;
import powers.LockoutPower;
import powers.SoarPower;

import java.util.Map;
import java.util.WeakHashMap;

public class FlightSelfDamagePatch {
    // Raw self-damage usually skips applyPowers. Apply flight/Lockout only if not already included.
    private static final Map<DamageInfo, Integer> APPLIED_FLIGHT = new WeakHashMap<>();

    private static boolean active(AbstractCreature player, String id) {
        AbstractPower power = player.getPower(id);
        return power != null && power.amount > 0;
    }

    public static int adjust(DamageInfo info, int damage, AbstractPlayer player) {
        if (info == null || info.owner != player || info.type == DamageInfo.DamageType.HP_LOSS || damage <= 0) {
            return damage;
        }
        int applied = APPLIED_FLIGHT.containsKey(info) ? APPLIED_FLIGHT.get(info) : 0;
        float result = damage;
        if ((applied & 1) == 0 && active(player, HoverPower.POWER_ID)) result /= 2.0F;
        if ((applied & 2) == 0 && active(player, SoarPower.POWER_ID)) result /= 2.0F;
        if ((applied & 4) == 0 && player.hasPower(LockoutPower.POWER_ID)) result *= 0.25F;
        return (int)Math.floor(result);
    }

    @SpirePatch(clz = DamageInfo.class, method = "applyPowers",
            paramtypez = {AbstractCreature.class, AbstractCreature.class})
    public static class TrackAppliedFlight {
        @SpirePostfixPatch
        public static void postfix(DamageInfo __instance, AbstractCreature owner, AbstractCreature target) {
            APPLIED_FLIGHT.remove(__instance);
            if (owner == target && target instanceof AbstractPlayer && __instance.type == DamageInfo.DamageType.NORMAL) {
                int mask = (active(target, HoverPower.POWER_ID) ? 1 : 0)
                        | (active(target, SoarPower.POWER_ID) ? 2 : 0)
                        | (target.hasPower(LockoutPower.POWER_ID) ? 4 : 0);
                APPLIED_FLIGHT.put(__instance, mask);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage", paramtypez = {DamageInfo.class})
    public static class BeforeBlock {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess field) throws CannotCompileException {
                    if (field.isReader() && DamageInfo.class.getName().equals(field.getClassName())
                            && "output".equals(field.getFieldName())) {
                        // Before Intangible and decrementBlock. Leave the shared DamageInfo unchanged.
                        field.replace("$_ = patches.FlightSelfDamagePatch.adjust($0, $proceed(), this);");
                    }
                }
            };
        }
    }
}
