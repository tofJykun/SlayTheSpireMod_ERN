package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.AttackPotion;
import com.megacrit.cardcrawl.potions.ColorlessPotion;
import com.megacrit.cardcrawl.potions.PowerPotion;
import com.megacrit.cardcrawl.potions.SkillPotion;
import general.PotionEffectHelper;

@SpirePatch(clz = AttackPotion.class, method = "initializeData")
@SpirePatch(clz = SkillPotion.class, method = "initializeData")
@SpirePatch(clz = PowerPotion.class, method = "initializeData")
@SpirePatch(clz = ColorlessPotion.class, method = "initializeData")
public class DiscoveryPotionDescriptionPatch {
    @SpirePostfixPatch
    public static void postfix(AbstractPotion __instance) {
        // Vanilla descriptions only check the relic, not the temporary Bark effect.
        if (!PotionEffectHelper.hasHaligtreeBalmOnlyEffect() || __instance.getPotency() <= 1) return;
        PotionStrings strings = CardCrawlGame.languagePack.getPotionString(__instance.ID);
        __instance.description = strings.DESCRIPTIONS[1];
        if (!__instance.tips.isEmpty()) {
            __instance.tips.get(0).body = __instance.description;
        }
    }
}
