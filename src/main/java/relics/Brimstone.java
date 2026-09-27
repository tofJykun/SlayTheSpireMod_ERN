package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Brimstone extends CustomRelic {
    // Keep the mod relic distinct from the base game's Brimstone relic.
    public static final String ID = "ERN_Brimstone";
    private static final String IMG = "img/relics/undertaker/Brimstone.png";
    private static final String IMG_OTL = "img/relics/undertaker/outline/Brimstone.png";
    private static final int STRENGTH = 1;

    public Brimstone() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.UNCOMMON, AbstractRelic.LandingSound.MAGICAL);
    }

    public void onFaithGain() {
        if (AbstractDungeon.player == null) {
            return;
        }
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                (AbstractCreature)AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player,
                new StrengthPower((AbstractCreature)AbstractDungeon.player, STRENGTH), STRENGTH));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Brimstone();
    }
}
