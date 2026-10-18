package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CombatState;
import general.PotionHistory;
import powers.ThinkingPlumPower;

@SpirePatch(clz = PotionHistory.class, method = "record", paramtypez = {String.class})
public class ThinkingPlumPatch {
    @SpirePostfixPatch
    public static void postfix(String potionId) {
        // The shared history records actual uses, including bulk/replayed/revival potions,
        // but never potion acquisition, discarding or cancelled target selection.
        if (potionId == null || potionId.isEmpty() || !CombatState.isInCombat()
                || AbstractDungeon.player == null) return;
        AbstractPower power = AbstractDungeon.player.getPower(ThinkingPlumPower.POWER_ID);
        if (power instanceof ThinkingPlumPower) ((ThinkingPlumPower)power).onPotionUsed();
    }
}
