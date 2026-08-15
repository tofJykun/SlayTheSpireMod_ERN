package relics;

import basemod.abstracts.CustomRelic;
import cards.tempcards.FadingPrimalGlintstone;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class RitualBand extends CustomRelic {
    public static final String ID = "RitualBand";
    private static final String IMG = "img/relics/recluse/RitualBand.png";
    private static final String IMG_OTL = "img/relics/recluse/outline/RitualBand.png";

    public RitualBand() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.UNCOMMON, AbstractRelic.LandingSound.MAGICAL);
    }

    public static void upgradeFadingPrimalGlintstone(AbstractCard card) {
        if (card == null || !FadingPrimalGlintstone.ID.equals(card.cardID) || card.upgraded || !isInCombat()) {
            return;
        }
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(ID)) {
            AbstractDungeon.player.getRelic(ID).flash();
            card.upgrade();
        }
    }

    private static boolean isInCombat() {
        return AbstractDungeon.currMapNode != null
                && AbstractDungeon.getCurrRoom() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RitualBand();
    }
}
