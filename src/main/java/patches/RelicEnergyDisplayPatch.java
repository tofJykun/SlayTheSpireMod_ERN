package patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup;
import general.RelicEnergyDisplay;

public class RelicEnergyDisplayPatch {
    @SpirePatch(clz = AbstractRelic.class, method = "renderTip")
    public static class Tip {
        @SpirePrefixPatch
        public static void prefix(AbstractRelic __instance) {
            RelicEnergyDisplay.prepare(__instance);
        }
    }

    @SpirePatch(clz = AbstractRelic.class, method = "renderBossTip")
    public static class BossTip {
        @SpirePrefixPatch
        public static void prefix(AbstractRelic __instance) {
            RelicEnergyDisplay.prepare(__instance);
        }
    }

    @SpirePatch(clz = SingleRelicViewPopup.class, method = "renderDescription")
    public static class Popup {
        @SpirePrefixPatch
        public static void prefix(SingleRelicViewPopup __instance) {
            AbstractRelic relic = ReflectionHacks.getPrivate(__instance, SingleRelicViewPopup.class, "relic");
            RelicEnergyDisplay.prepare(relic);
        }
    }

    @SpirePatch(clz = FontHelper.class, method = "identifyOrb")
    public static class IdentifyOrb {
        @SpirePrefixPatch
        public static SpireReturn<TextureAtlas.AtlasRegion> prefix(String word) {
            TextureAtlas.AtlasRegion orb = RelicEnergyDisplay.orb(word);
            return orb == null ? SpireReturn.Continue() : SpireReturn.Return(orb);
        }
    }
}
