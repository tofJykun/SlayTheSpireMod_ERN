package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import general.SummonKeywordHelper;

import java.util.ArrayList;

public class SummonKeywordPatch {
    @SpirePatch(clz = TipHelper.class, method = "renderTipForCard")
    public static class CardTips {
        @SpirePrefixPatch
        public static void prefix() {
            SummonKeywordHelper.refreshDictionary();
        }
    }

    @SpirePatch(clz = TipHelper.class, method = "queuePowerTips")
    public static class PowerTips {
        @SpirePrefixPatch
        public static void prefix(float x, float y, ArrayList<PowerTip> powerTips) {
            // Also refresh cached tips and the large card view before layout is calculated.
            for (PowerTip tip : powerTips) {
                SummonKeywordHelper.refreshTip(tip);
            }
        }
    }
}
