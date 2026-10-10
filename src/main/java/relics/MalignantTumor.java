package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import powers.CalamitySpraymistPower;

public class MalignantTumor extends CustomRelic {
    public static final String ID = "MalignantTumor";

    public MalignantTumor() {
        super(ID, ImageMaster.loadImage("img/relics/executor/MalignantTumor.png"),
                ImageMaster.loadImage("img/relics/executor/outline/MalignantTumor.png"),
                RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new CalamitySpraymistPower(AbstractDungeon.player, 1), 1));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MalignantTumor();
    }
}
