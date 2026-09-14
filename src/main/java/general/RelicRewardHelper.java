package general;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public final class RelicRewardHelper {
    private RelicRewardHelper() {
    }

    public static AbstractRelic returnRandomEliteDropRelic() {
        return AbstractDungeon.returnRandomRelic(returnRandomEliteDropRelicTier());
    }

    public static AbstractRelic.RelicTier returnRandomEliteDropRelicTier() {
        AbstractRelic.RelicTier tier = AbstractDungeon.returnRandomRelicTier();
        return isEliteDropRelicTier(tier) ? tier : AbstractRelic.RelicTier.UNCOMMON;
    }

    public static boolean isEliteDropRelicTier(AbstractRelic.RelicTier tier) {
        return tier == AbstractRelic.RelicTier.COMMON
                || tier == AbstractRelic.RelicTier.UNCOMMON
                || tier == AbstractRelic.RelicTier.RARE;
    }
}
