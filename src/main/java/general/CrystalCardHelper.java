package general;

import cards.recluse.CrystalRingShield;
import cards.recluse.CrystalScroll;
import cards.recluse.CrystalScrap;
import cards.recluse.CrystalSoulSpear;
import cards.recluse.GravityCrystal;
import cards.recluse.HomingCrystalSoulmass;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CrystalCardHelper {
    private static final ArrayList<String> CRYSTAL_CARD_IDS = new ArrayList<>();

    static {
        CRYSTAL_CARD_IDS.add(CrystalSoulSpear.ID);
        CRYSTAL_CARD_IDS.add(CrystalScroll.ID);
        CRYSTAL_CARD_IDS.add(CrystalRingShield.ID);
        CRYSTAL_CARD_IDS.add(HomingCrystalSoulmass.ID);
        CRYSTAL_CARD_IDS.add(CrystalScrap.ID);
        CRYSTAL_CARD_IDS.add(GravityCrystal.ID);
    }

    private CrystalCardHelper() {
    }

    public static List<String> getCrystalCardIds() {
        return Collections.unmodifiableList(CRYSTAL_CARD_IDS);
    }

    public static boolean isCrystalCard(AbstractCard card) {
        return card != null && CRYSTAL_CARD_IDS.contains(card.cardID);
    }

    public static AbstractCard randomCrystalCard() {
        if (CRYSTAL_CARD_IDS.isEmpty()) {
            return null;
        }
        int index = AbstractDungeon.cardRandomRng == null
                ? 0
                : AbstractDungeon.cardRandomRng.random(CRYSTAL_CARD_IDS.size() - 1);
        return CardLibrary.getCopy(CRYSTAL_CARD_IDS.get(index));
    }
}
