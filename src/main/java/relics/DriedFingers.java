package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class DriedFingers extends CustomRelic {
    public static final String ID = "DriedFingers";
    private static final int BLOCK = 3;

    public DriedFingers() {
        super(ID, ImageMaster.loadImage("img/relics/revenant/DriedFingers.png"),
                ImageMaster.loadImage("img/relics/revenant/outline/DriedFingers.png"),
                RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    public void onSummon() {
        if (AbstractDungeon.player == null) return;
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, BLOCK));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DriedFingers();
    }
}
