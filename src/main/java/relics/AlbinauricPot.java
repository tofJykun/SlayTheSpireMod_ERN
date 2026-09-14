package relics;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import general.BulkPotionQueue;
import general.CombatState;
import powers.ArcanePower;

public class AlbinauricPot extends CustomRelic {
    public static final String ID = "AlbinauricPot";
    private static final String IMG = "img/relics/scholar/AlbinauricPot.png";
    private static final String IMG_OTL = "img/relics/scholar/outline/AlbinauricPot.png";
    private static final int ARCANE = 1;

    public AlbinauricPot() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.UNCOMMON, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void onUsePotion() {
        if (!CombatState.isInCombat() || AbstractDungeon.player == null) {
            return;
        }

        AbstractPotion potion = currentPotion();
        if (potion == null || BulkPotionQueue.isBulkPotion(potion.ID)) {
            return;
        }

        flash();
        addToBot((AbstractGameAction)new RelicAboveCreatureAction(
                (AbstractCreature)AbstractDungeon.player, this));
        addToBot((AbstractGameAction)new ApplyPowerAction(
                (AbstractCreature)AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player,
                new ArcanePower((AbstractCreature)AbstractDungeon.player, ARCANE), ARCANE));
    }

    private static AbstractPotion currentPotion() {
        if (AbstractDungeon.topPanel == null || AbstractDungeon.topPanel.potionUi == null) {
            return null;
        }
        return ReflectionHacks.getPrivate(AbstractDungeon.topPanel.potionUi, PotionPopUp.class, "potion");
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new AlbinauricPot();
    }
}
