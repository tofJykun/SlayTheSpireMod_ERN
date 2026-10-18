package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class LargePouch extends CustomRelic {
    public static final String ID = "LargePouch";
    private static final int POTION_SLOTS = 3;
    private boolean pickupEffectTriggered;

    public LargePouch() {
        super(ID, ImageMaster.loadImage("img/relics/scholar/LargePouch.png"),
                ImageMaster.loadImage("img/relics/scholar/outline/LargePouch.png"),
                RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override public boolean canSpawn() {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(Bagcraft.ID);
    }

    @Override public void bossObtainLogic() {
        if (canSpawn()) {
            int slot = AbstractDungeon.player.relics.indexOf(AbstractDungeon.player.getRelic(Bagcraft.ID));
            instantObtain(AbstractDungeon.player, slot, true);
        } else {
            instantObtain();
        }
    }

    @Override public void onEquip() {
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null || pickupEffectTriggered || AbstractDungeon.potionRng == null) return;
        if (PotionHelper.potions.isEmpty()) PotionHelper.initialize(player.chosenClass);

        // Only fill the new slots; Bagcraft's slots and their contents are permanent.
        int firstNewSlot = player.potionSlots;
        player.potionSlots += POTION_SLOTS;
        for (int i = 0; i < POTION_SLOTS; i++) {
            int slot = firstNewSlot + i;
            while (player.potions.size() <= slot) {
                player.potions.add(new PotionSlot(player.potions.size()));
            }
            player.obtainPotion(slot, PotionHelper.getRandomPotion());
        }
        pickupEffectTriggered = true;
    }

    @Override public String getUpdatedDescription() { return DESCRIPTIONS[0]; }

    @Override public AbstractRelic makeCopy() { return new LargePouch(); }
}
