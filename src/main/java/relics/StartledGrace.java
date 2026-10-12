package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class StartledGrace extends CustomRelic {
    public static final String ID = "StartledGrace";

    public StartledGrace() {
        super(ID, ImageMaster.loadImage("img/relics/duchess/StartledGrace.png"),
                ImageMaster.loadImage("img/relics/duchess/outline/StartledGrace.png"),
                RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(MagnificentPoise.ID);
    }

    @Override
    public void bossObtainLogic() {
        if (canSpawn()) {
            int slot = AbstractDungeon.player.relics.indexOf(AbstractDungeon.player.getRelic(MagnificentPoise.ID));
            instantObtain(AbstractDungeon.player, slot, true);
        } else {
            instantObtain();
        }
    }

    @Override
    public void atBattleStart() { this.counter = 0; }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        this.counter++;
        if (this.counter == 3) {
            this.counter = 0;
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 6));
        }
    }

    @Override
    public void onVictory() { this.counter = -1; }

    @Override
    public String getUpdatedDescription() { return DESCRIPTIONS[0]; }

    @Override
    public AbstractRelic makeCopy() { return new StartledGrace(); }
}
