package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import powers.SpiritPower;

public class SacredTimber extends CustomRelic {
    public static final String ID = "SacredTimber";
    private static final String IMG = "img/relics/revenant/SacredTimber.png";
    private static final String IMG_OTL = "img/relics/revenant/outline/SacredTimber.png";
    private static final int SPIRIT = 1;

    public SacredTimber() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.COMMON, AbstractRelic.LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction(
                (AbstractCreature)AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player,
                new SpiritPower((AbstractCreature)AbstractDungeon.player, SPIRIT), SPIRIT));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SacredTimber();
    }
}
