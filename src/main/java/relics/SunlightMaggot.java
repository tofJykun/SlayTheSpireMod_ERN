package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SunlightMaggot extends CustomRelic {
    public static final String ID = "SunlightMaggot";
    private static final String IMG = "img/relics/guardian/SunlightMaggot.png";
    private static final String IMG_OTL = "img/relics/guardian/outline/SunlightMaggot.png";
    private boolean available;

    public SunlightMaggot() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public void atPreBattle() {
        atTurnStart();
    }

    @Override
    public void atTurnStart() {
        available = true;
        grayscale = false;
    }

    public static void onVigorUsed(AbstractCreature owner, AbstractCard card, int vigor) {
        if (owner == null || owner != AbstractDungeon.player || vigor <= 0
                || card == null || card.type != AbstractCard.CardType.ATTACK) return;
        AbstractRelic relic = AbstractDungeon.player.getRelic(ID);
        if (relic instanceof SunlightMaggot) ((SunlightMaggot)relic).trigger();
    }

    private void trigger() {
        if (!available) return;
        available = false;
        grayscale = true;
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new GainEnergyAction(1));
    }

    @Override
    public void onVictory() {
        available = false;
        grayscale = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SunlightMaggot();
    }
}
