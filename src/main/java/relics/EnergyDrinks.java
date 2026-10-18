package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import general.CombatState;

public class EnergyDrinks extends CustomRelic {
    public static final String ID = "EnergyDrinks";
    private boolean available;

    public EnergyDrinks() {
        super(ID, ImageMaster.loadImage("img/relics/scholar/EnergyDrinks.png"),
                ImageMaster.loadImage("img/relics/scholar/outline/EnergyDrinks.png"),
                RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override public void onEquip() { atTurnStart(); }

    @Override public void atPreBattle() { atTurnStart(); }

    @Override public void atTurnStart() {
        available = true;
        grayscale = false;
    }

    @Override public void onUsePotion() {
        if (!available || AbstractDungeon.player == null || !CombatState.isInCombat()) return;
        available = false;
        grayscale = true;
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new GainEnergyAction(1));
    }

    @Override public void onVictory() {
        available = false;
        grayscale = false;
    }

    @Override public String getUpdatedDescription() { return DESCRIPTIONS[0]; }

    @Override public AbstractRelic makeCopy() { return new EnergyDrinks(); }
}
