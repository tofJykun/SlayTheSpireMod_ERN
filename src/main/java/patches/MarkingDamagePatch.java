package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.MarkingPower;
import powers.PoisonMarkingPower;

import java.util.Map;
import java.util.WeakHashMap;

public class MarkingDamagePatch {
    private static final Map<AbstractCreature, TurnDamage> DAMAGE = new WeakHashMap<>();

    private static class TurnDamage {
        final int turn = GameActionManager.turn;
        int amount;
    }

    public static int getDamageThisTurn(AbstractCreature monster) {
        TurnDamage damage = DAMAGE.get(monster);
        return damage != null && damage.turn == GameActionManager.turn ? damage.amount : 0;
    }

    @SpirePatch(clz = AbstractCreature.class, method = "applyEndOfTurnTriggers")
    public static class PlayerEndTurn {
        @SpirePostfixPatch
        public static void postfix(AbstractCreature __instance) {
            if (!(__instance instanceof AbstractPlayer) || AbstractDungeon.getMonsters() == null) {
                return;
            }
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                AbstractPower power = monster.getPower(MarkingPower.POWER_ID);
                if (power instanceof MarkingPower) {
                    ((MarkingPower)power).onPlayerEndTurn();
                }
                AbstractPower poisonMarking = monster.getPower(PoisonMarkingPower.POWER_ID);
                if (poisonMarking instanceof PoisonMarkingPower) {
                    ((PoisonMarkingPower)poisonMarking).onPlayerEndTurn();
                }
            }
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "decrementBlock",
            paramtypez = { DamageInfo.class, int.class })
    public static class RecordDamage {
        @SpirePrefixPatch
        public static void prefix(AbstractCreature __instance, DamageInfo info, int damageAmount) {
            if (!(__instance instanceof AbstractMonster) || __instance.isDeadOrEscaped() || damageAmount <= 0) {
                return;
            }
            // Record before block subtraction, even before Marking has been applied.
            TurnDamage damage = DAMAGE.get(__instance);
            if (damage == null || damage.turn != GameActionManager.turn) {
                damage = new TurnDamage();
                DAMAGE.put(__instance, damage);
            }
            damage.amount = (int)Math.min(Integer.MAX_VALUE, (long)damage.amount + damageAmount);
            AbstractPower marking = __instance.getPower(MarkingPower.POWER_ID);
            if (marking != null) {
                marking.updateDescription();
            }
            AbstractPower poisonMarking = __instance.getPower(PoisonMarkingPower.POWER_ID);
            if (poisonMarking != null) {
                poisonMarking.updateDescription();
            }
        }
    }
}
