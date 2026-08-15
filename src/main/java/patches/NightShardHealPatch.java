package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.NightShard;

import java.util.ArrayDeque;

@SpirePatch(clz = AbstractCreature.class, method = "heal", paramtypez = {int.class, boolean.class})
public class NightShardHealPatch {
    private static final ArrayDeque<Integer> HEALTH_BEFORE = new ArrayDeque<>();

    @SpirePrefixPatch
    public static void Prefix(AbstractCreature __instance, int healAmount, boolean showEffect) {
        HEALTH_BEFORE.push(__instance.currentHealth);
    }

    @SpirePostfixPatch
    public static void Postfix(AbstractCreature __instance, int healAmount, boolean showEffect) {
        int healthBefore = HEALTH_BEFORE.isEmpty() ? __instance.currentHealth : HEALTH_BEFORE.pop();
        if (!__instance.isPlayer || NightShard.isGainingMaxHp() || AbstractDungeon.player == null) {
            return;
        }
        if (__instance.currentHealth <= healthBefore || !AbstractDungeon.player.hasRelic(NightShard.ID)) {
            return;
        }

        AbstractRelic relic = AbstractDungeon.player.getRelic(NightShard.ID);
        if (relic instanceof NightShard) {
            ((NightShard)relic).onActualHeal();
        }
    }
}
