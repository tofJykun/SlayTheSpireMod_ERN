package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CombatState;
import powers.GetLostPower;

public class GetLostPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static final SpireField<Boolean> endOfTurn = new SpireField<>(() -> false);
    }

    public static boolean triggersAtEnd() {
        return AbstractDungeon.player != null && Fields.endOfTurn.get(AbstractDungeon.player);
    }

    public static void enableEndOfTurn(AbstractPlayer player) {
        Fields.endOfTurn.set(player, true);
        if (!CombatState.isInCombat()) return;
        for (AbstractMonster monster : CombatState.currentRoom().monsters.monsters) {
            AbstractPower power = monster.getPower(GetLostPower.POWER_ID);
            if (power != null) power.updateDescription();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep")
    public static class Reset {
        @SpirePrefixPatch
        public static void prefix(AbstractPlayer __instance) { Fields.endOfTurn.set(__instance, false); }
    }

    private static void trigger(AbstractCreature creature, boolean end) {
        if (creature != AbstractDungeon.player || !CombatState.isInCombat()) return;
        for (AbstractMonster monster : CombatState.currentRoom().monsters.monsters) {
            AbstractPower power = monster.getPower(GetLostPower.POWER_ID);
            if (power instanceof GetLostPower) ((GetLostPower)power).onPlayerTurnBoundary(end);
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "applyStartOfTurnPowers")
    public static class Start {
        @SpirePostfixPatch
        public static void postfix(AbstractCreature __instance) { trigger(__instance, false); }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "applyEndOfTurnTriggers")
    public static class End {
        @SpirePostfixPatch
        public static void postfix(AbstractCreature __instance) { trigger(__instance, true); }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "healthBarUpdatedEvent", paramtypez = {})
    public static class Health {
        @SpirePostfixPatch
        public static void postfix(AbstractCreature __instance) {
            AbstractPower power = __instance.getPower(GetLostPower.POWER_ID);
            if (power instanceof GetLostPower) ((GetLostPower)power).onHealthChanged();
        }
    }
}
