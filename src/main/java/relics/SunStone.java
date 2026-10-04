package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import powers.ArcanePower;

public class SunStone extends CustomRelic {
    public static final String ID = "SunStone";

    public SunStone() {
        super(ID, ImageMaster.loadImage("img/relics/shared/SunStone.png"),
                ImageMaster.loadImage("img/relics/shared/outline/SunStone.png"),
                RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public void onEquip() {
        CardCrawlGame.sound.play("GOLD_GAIN");
        AbstractDungeon.player.gainGold(999);
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new StrengthPower(AbstractDungeon.player, 3), 3));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new DexterityPower(AbstractDungeon.player, 3), 3));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new ArcanePower(AbstractDungeon.player, 3), 3));
    }

    @Override
    public boolean canSpawn() {
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SunStone();
    }
}
