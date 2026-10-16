package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import variables.UpgradePreviewVariable;

public final class CustomVariablePreviewPatch {
    @SpirePatch(clz = AbstractCard.class, method = "resetAttributes")
    @SpirePatch(clz = AbstractCard.class, method = "applyPowers")
    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    @SpirePatch(clz = AbstractCard.class, method = "displayUpgrades")
    public static class Reset {
        @SpirePrefixPatch
        public static void prefix(AbstractCard __instance) {
            UpgradePreviewVariable.reset(__instance);
        }
    }
}
