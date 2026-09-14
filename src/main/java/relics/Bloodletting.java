package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import powers.GreyHealthPlusPower;
import powers.GreyHealthPower;

public class Bloodletting extends CustomRelic {
    public static final String ID = "Bloodletting";
    private static final String IMG = "img/relics/raider/Bloodletting.png";
    private static final String IMG_OTL = "img/relics/raider/outline/Bloodletting.png";

    public Bloodletting() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onExhaust(AbstractCard card) {
        if (card == null || AbstractDungeon.player == null || AbstractDungeon.actionManager == null) {
            return;
        }
        String powerId = hasFighterDestined() ? GreyHealthPlusPower.POWER_ID : GreyHealthPower.POWER_ID;
        flash();
        // Resolve by ID so several exhausts queued together cannot reduce a removed power instance.
        addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, powerId, 1));
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public void update() {
        super.update();
        String updated = getUpdatedDescription();
        if (!updated.equals(this.description)) {
            this.description = updated;
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            initializeTips();
        }
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[hasFighterDestined() ? 1 : 0];
    }

    private static boolean hasFighterDestined() {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(FighterDestined.ID);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Bloodletting();
    }
}
