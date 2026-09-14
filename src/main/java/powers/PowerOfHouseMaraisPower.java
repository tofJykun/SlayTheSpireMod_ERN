package powers;

import cards.raider.PowerOfHouseMarais;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;
import java.util.UUID;

public class PowerOfHouseMaraisPower extends AbstractPower {
    public static final String POWER_ID = "PowerOfHouseMaraisPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final UUID cardUuid;

    public PowerOfHouseMaraisPower(AbstractCreature owner, UUID cardUuid, int strengthAmount) {
        this.name = POWER_STRINGS.NAME;
        this.ID = POWER_ID + ":" + cardUuid;
        this.owner = owner;
        this.cardUuid = cardUuid;
        this.amount = strengthAmount;
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

    public static void onMonsterKilled(AbstractMonster monster) {
        if (monster == null || monster.halfDead || !isInCombat()) {
            return;
        }
        adjustActivePowers(PowerOfHouseMarais.getKillIncrease());
    }

    public static void onPlayerLostHp(AbstractCreature player, int lostHp) {
        if (player == null || lostHp <= 0 || !isInCombat()) {
            return;
        }
        adjustActivePowers(PowerOfHouseMarais.getHpLossDecrease());
    }

    private static void adjustActivePowers(int delta) {
        if (AbstractDungeon.player == null) {
            return;
        }
        ArrayList<AbstractPower> powers = new ArrayList<>(AbstractDungeon.player.powers);
        for (AbstractPower power : powers) {
            if (power instanceof PowerOfHouseMaraisPower) {
                ((PowerOfHouseMaraisPower)power).adjustPermanentValue(delta);
            }
        }
    }

    private void adjustPermanentValue(int delta) {
        int newStrength = PowerOfHouseMarais.adjustPermanentStrength(this.cardUuid, delta);
        if (newStrength >= 0) {
            this.amount = newStrength;
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
