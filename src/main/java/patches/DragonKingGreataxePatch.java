package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import powers.DragonKingGreataxePower;

public final class DragonKingGreataxePatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "obtainPotion", paramtypez = {AbstractPotion.class})
    public static class Obtain {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> prefix() {
            return DragonKingGreataxePower.blocksPotions() ? SpireReturn.Return(false) : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "obtainPotion", paramtypez = {int.class, AbstractPotion.class})
    public static class ObtainInSlot {
        @SpirePrefixPatch
        public static SpireReturn<Void> prefix() {
            return DragonKingGreataxePower.blocksPotions() ? SpireReturn.Return(null) : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ObtainPotionAction.class, method = "update")
    public static class ObtainAction {
        @SpirePrefixPatch
        public static SpireReturn<Void> prefix(ObtainPotionAction __instance) {
            if (DragonKingGreataxePower.blocksPotions()) {
                __instance.isDone = true;
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
