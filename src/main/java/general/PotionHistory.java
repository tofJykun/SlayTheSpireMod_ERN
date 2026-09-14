package general;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import potions.ElixirOfLife;

import java.util.ArrayList;

public final class PotionHistory {
    private static final ArrayList<String> USED_POTION_IDS = new ArrayList<>();

    private PotionHistory() {
    }

    public static void resetCombat() {
        USED_POTION_IDS.clear();
    }

    public static void record(AbstractPotion potion) {
        if (potion != null) {
            record(potion.ID);
        }
    }

    public static void record(String potionId) {
        if (potionId != null && !potionId.isEmpty()) {
            USED_POTION_IDS.add(potionId);
        }
    }

    public static ArrayList<String> snapshot() {
        return new ArrayList<>(USED_POTION_IDS);
    }

    public static void replayPotion(String potionId, AbstractMonster preferredTarget) {
        AbstractPotion potion = PotionHelper.getPotion(potionId);
        if (potion == null || AbstractDungeon.player == null) {
            return;
        }

        record(potion);
        GuidanceStats.recordPotionUse();

        if ("FairyPotion".equals(potion.ID)) {
            restoreToAtLeastHalfHealth();
        } else if (ElixirOfLife.POTION_ID.equals(potion.ID)) {
            restoreToFullHealth();
        } else {
            potion.slot = -1;
            potion.use(targetFor(potion, preferredTarget));
        }

        triggerPotionRelics(potion);
    }

    private static AbstractCreature targetFor(AbstractPotion potion, AbstractMonster preferredTarget) {
        if (!potion.targetRequired) {
            return null;
        }
        if (preferredTarget != null && !preferredTarget.isDeadOrEscaped()) {
            return preferredTarget;
        }
        if (AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getCurrRoom().monsters == null) {
            return null;
        }
        return AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
    }

    private static void restoreToAtLeastHalfHealth() {
        int targetHealth = (int)Math.ceil(AbstractDungeon.player.maxHealth * 0.5F);
        if (targetHealth > AbstractDungeon.player.currentHealth) {
            AbstractDungeon.player.heal(targetHealth - AbstractDungeon.player.currentHealth, true);
        }
    }

    private static void restoreToFullHealth() {
        if (AbstractDungeon.player.maxHealth > AbstractDungeon.player.currentHealth) {
            AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth - AbstractDungeon.player.currentHealth, true);
        }
    }

    private static void triggerPotionRelics(AbstractPotion potion) {
        if (!CombatState.isInCombat() || AbstractDungeon.player == null) {
            return;
        }

        AbstractPotion previousPotion = null;
        boolean hasPotionUi = AbstractDungeon.topPanel != null && AbstractDungeon.topPanel.potionUi != null;
        if (hasPotionUi) {
            previousPotion = ReflectionHacks.getPrivate(AbstractDungeon.topPanel.potionUi, PotionPopUp.class, "potion");
            ReflectionHacks.setPrivate(AbstractDungeon.topPanel.potionUi, PotionPopUp.class, "potion", potion);
        }
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            relic.onUsePotion();
        }
        if (hasPotionUi) {
            ReflectionHacks.setPrivate(AbstractDungeon.topPanel.potionUi, PotionPopUp.class, "potion", previousPotion);
        }

        if (AbstractDungeon.getCurrRoom() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.utility.HandCheckAction());
        }
    }
}
