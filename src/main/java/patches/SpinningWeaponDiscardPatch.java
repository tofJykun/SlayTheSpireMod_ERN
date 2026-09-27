package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.SpinningWeaponPower;

@SpirePatch(clz = GameActionManager.class, method = "incrementDiscard", paramtypez = {boolean.class})
public class SpinningWeaponDiscardPatch {
    @SpirePrefixPatch
    public static void prefix(boolean endOfTurn) {
        if (endOfTurn || AbstractDungeon.actionManager == null
                || AbstractDungeon.actionManager.turnHasEnded || AbstractDungeon.player == null) {
            return;
        }
        AbstractPower power = AbstractDungeon.player.getPower(SpinningWeaponPower.POWER_ID);
        if (power instanceof SpinningWeaponPower) {
            ((SpinningWeaponPower)power).onDiscard();
        }
    }
}
