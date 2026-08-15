package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import general.BulkPotionQueue;

public class KleinBottle extends CustomRelic {
    public static final String ID = "KleinBottle";
    private static final String IMG = "img/relics/scholar/KleinBottle.png";
    private static final String IMG_OTL = "img/relics/scholar/outline/KleinBottle.png";

    public KleinBottle() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.RARE, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        AbstractPotion potion = BulkPotionQueue.getRandomBulkPotion();
        if (potion != null && hasEmptyPotionSlot()) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ObtainPotionAction(potion));
        }
    }

    private static boolean hasEmptyPotionSlot() {
        if (AbstractDungeon.player == null) {
            return false;
        }
        int checkedSlots = Math.min(AbstractDungeon.player.potionSlots, AbstractDungeon.player.potions.size());
        for (int i = 0; i < checkedSlots; i++) {
            if (AbstractDungeon.player.potions.get(i) instanceof PotionSlot) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new KleinBottle();
    }
}
