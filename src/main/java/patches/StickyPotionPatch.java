package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CombatState;
import general.PotionHistory;
import powers.StickyPotionPower;

@SpirePatch(clz = PotionHistory.class, method = "record", paramtypez = {String.class})
public class StickyPotionPatch {
    @SpirePostfixPatch
    public static void postfix(String potionId) {
        if (potionId == null || potionId.isEmpty() || !CombatState.isInCombat()
                || AbstractDungeon.player == null) return;
        AbstractPower power = AbstractDungeon.player.getPower(StickyPotionPower.POWER_ID);
        if (power instanceof StickyPotionPower) ((StickyPotionPower)power).onPotionUsed();
    }
}
