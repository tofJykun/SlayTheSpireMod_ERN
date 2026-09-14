package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.PlayerHpLossHelper;
import powers.PowerOfHouseMaraisPower;
import powers.PowerOfVengeancePower;

public class PowerOfVengeancePatch {
    @SpirePatch(clz = AbstractMonster.class, method = "die", paramtypez = { boolean.class })
    public static class MonsterDiePatch {
        @SpirePostfixPatch
        public static void postfix(AbstractMonster __instance, boolean triggerRelics) {
            PowerOfVengeancePower.onMonsterKilled(__instance);
            PowerOfHouseMaraisPower.onMonsterKilled(__instance);
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage", paramtypez = { DamageInfo.class })
    public static class PlayerDamagePatch {
        @SpirePostfixPatch
        public static void postfix(AbstractPlayer __instance, DamageInfo info) {
            PlayerHpLossHelper.onPlayerLostHp(__instance, __instance.lastDamageTaken);
        }
    }
}
