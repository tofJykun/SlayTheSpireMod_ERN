package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Bagcraft extends CustomRelic {
    public static final String ID = "Bagcraft";
    private static final String IMG = "img/relics/scholar/Bagcraft.png";
    private static final String IMG_OTL = "img/relics/scholar/outline/Bagcraft.png";
    private static final int POTION_SLOTS = 3;
    private boolean pickupEffectTriggered = false;

    public Bagcraft() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.STARTER, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void onEquip() {
        triggerPickupEffect(AbstractDungeon.player);
    }

    @Override
    public void instantObtain(AbstractPlayer p, int slot, boolean callOnEquip) {
        super.instantObtain(p, slot, callOnEquip);
    }

    private void triggerPickupEffect(AbstractPlayer player) {
        if (player == null || this.pickupEffectTriggered || AbstractDungeon.potionRng == null) {
            return;
        }

        if (PotionHelper.potions.isEmpty()) {
            PotionHelper.initialize(player.chosenClass);
        }
        int firstNewSlot = player.potionSlots;
        player.potionSlots += POTION_SLOTS;
        for (int i = 0; i < POTION_SLOTS; i++) {
            int slot = firstNewSlot + i;
            while (player.potions.size() <= slot) {
                player.potions.add(new PotionSlot(player.potions.size()));
            }
            player.obtainPotion(slot, PotionHelper.getRandomPotion());
        }
        this.pickupEffectTriggered = true;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Bagcraft();
    }
}
