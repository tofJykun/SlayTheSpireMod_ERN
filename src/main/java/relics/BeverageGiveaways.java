package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import general.CombatState;

public class BeverageGiveaways extends CustomRelic {
    public static final String ID = "BeverageGiveaways";
    private static final String IMG = "img/relics/scholar/BeverageGiveaways.png";
    private static final String IMG_OTL = "img/relics/scholar/outline/BeverageGiveaways.png";
    private static final int POTION_THRESHOLD = 3;
    private static final int STRENGTH = 1;
    private static final int DEXTERITY = 1;

    public BeverageGiveaways() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.UNCOMMON, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
        stopPulse();
    }

    @Override
    public void atTurnStart() {
        this.counter = 0;
        stopPulse();
    }

    @Override
    public void onUsePotion() {
        if (!CombatState.isInCombat() || AbstractDungeon.player == null) {
            return;
        }

        this.counter++;
        if (this.counter >= POTION_THRESHOLD) {
            this.counter = 0;
            stopPulse();
            flash();
            addToBot((AbstractGameAction)new RelicAboveCreatureAction(
                    (AbstractCreature)AbstractDungeon.player, this));
            addToBot((AbstractGameAction)new ApplyPowerAction(
                    (AbstractCreature)AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player,
                    new StrengthPower((AbstractCreature)AbstractDungeon.player, STRENGTH), STRENGTH));
            addToBot((AbstractGameAction)new ApplyPowerAction(
                    (AbstractCreature)AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player,
                    new DexterityPower((AbstractCreature)AbstractDungeon.player, DEXTERITY), DEXTERITY));
        } else if (this.counter == POTION_THRESHOLD - 1) {
            beginLongPulse();
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        stopPulse();
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BeverageGiveaways();
    }
}
