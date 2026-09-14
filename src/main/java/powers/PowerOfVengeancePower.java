package powers;

import cards.revenant.PowerOfVengeance;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;
import java.util.UUID;

public class PowerOfVengeancePower extends AbstractPower {
    public static final String POWER_ID = "PowerOfVengeancePower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final UUID cardUuid;

    public PowerOfVengeancePower(AbstractCreature owner, UUID cardUuid, int blockTarget) {
        this.name = POWER_STRINGS.NAME;
        this.ID = POWER_ID + ":" + cardUuid;
        this.owner = owner;
        this.cardUuid = cardUuid;
        this.amount = blockTarget;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        this.isTurnBased = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount = Math.max(this.amount, stackAmount);
        updateDescription();
    }

    public static boolean hasPowerOfVengeance(AbstractPlayer player) {
        return getBestBlockTarget(player) >= 0;
    }

    public static int getBestBlockTarget(AbstractPlayer player) {
        if (player == null) {
            return -1;
        }
        int best = -1;
        for (AbstractPower power : player.powers) {
            if (power instanceof PowerOfVengeancePower) {
                best = Math.max(best, power.amount);
            }
        }
        return best;
    }

    public static void onMonsterKilled(AbstractMonster monster) {
        if (monster == null || monster.halfDead || !isInCombat()) {
            return;
        }
        adjustActivePowers(PowerOfVengeance.getKillIncrease());
    }

    public static void onPlayerLostHp(AbstractPlayer player, int lostHp) {
        if (player == null || lostHp <= 0 || !isInCombat()) {
            return;
        }
        adjustActivePowers(PowerOfVengeance.getHpLossDecrease());
    }

    private static void adjustActivePowers(int delta) {
        if (AbstractDungeon.player == null) {
            return;
        }
        ArrayList<AbstractPower> powers = new ArrayList<>(AbstractDungeon.player.powers);
        for (AbstractPower power : powers) {
            if (power instanceof PowerOfVengeancePower) {
                ((PowerOfVengeancePower)power).adjustPermanentValue(delta);
            }
        }
    }

    private void adjustPermanentValue(int delta) {
        int newTarget = PowerOfVengeance.adjustPermanentBlockTarget(this.cardUuid, delta);
        if (newTarget >= 0) {
            this.amount = newTarget;
        } else {
            this.amount = Math.max(0, this.amount + delta);
        }
        flash();
        updateDescription();
    }

    private static boolean isInCombat() {
        try {
            return AbstractDungeon.getCurrRoom() != null
                    && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    public void updateDescription() {
        this.description = POWER_STRINGS.DESCRIPTIONS[0] + this.amount + POWER_STRINGS.DESCRIPTIONS[1];
    }
}
