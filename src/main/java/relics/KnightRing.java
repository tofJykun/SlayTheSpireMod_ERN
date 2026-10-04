package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class KnightRing extends CustomRelic {
    public static final String ID = "KnightRing";
    private static final int STRENGTH = 3;

    public KnightRing() {
        super(ID, ImageMaster.loadImage("img/relics/wylder/KnightRing.png"),
                ImageMaster.loadImage("img/relics/wylder/outline/KnightRing.png"),
                RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    public void onSuccessfulParry() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player,
                AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, STRENGTH), STRENGTH));
    }

    @Override
    public String getUpdatedDescription() { return DESCRIPTIONS[0]; }

    @Override
    public AbstractRelic makeCopy() { return new KnightRing(); }
}
