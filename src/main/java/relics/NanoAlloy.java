package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import powers.PlatingPower;

public class NanoAlloy extends CustomRelic {
    public static final String ID = "NanoAlloy";
    private static final int PLATING = 5;

    public NanoAlloy() {
        super(ID, ImageMaster.loadImage("img/relics/wylder/NanoAlloy.png"),
                ImageMaster.loadImage("img/relics/wylder/outline/NanoAlloy.png"),
                RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player,
                AbstractDungeon.player, new PlatingPower(AbstractDungeon.player, PLATING), PLATING));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new NanoAlloy();
    }
}
