package powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.Collections;

public class PowerOfCompassionPower extends AbstractPower {
    public static final String POWER_ID = "PowerOfCompassionPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static boolean synchronizing;

    public PowerOfCompassionPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount = -1;
        this.fontScale = 8.0F;
    }

    @Override
    public void updateDescription() {
        this.description = STRINGS.DESCRIPTIONS[0];
    }

    public static void synchronizeAll() {
        if (synchronizing || AbstractDungeon.player == null || AbstractDungeon.currMapNode == null
                || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT
                || AbstractDungeon.getMonsters() == null) {
            return;
        }
        synchronizing = true;
        try {
            AbstractPower playerStrength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID);
            int desired = playerStrength == null ? 0 : playerStrength.amount;
            boolean changed = false;
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (monster.isDeadOrEscaped() || monster.halfDead) {
                    continue;
                }
                AbstractPower power = monster.getPower(POWER_ID);
                if (power instanceof PowerOfCompassionPower) {
                    changed |= ((PowerOfCompassionPower)power).alignStrength(desired);
                }
            }
            if (changed) {
                AbstractDungeon.onModifyPower();
            }
        } finally {
            synchronizing = false;
        }
    }

    private boolean alignStrength(int desired) {
        AbstractPower strength = this.owner.getPower(StrengthPower.POWER_ID);
        if (strength == null && desired == 0) {
            return false;
        }
        // Called outside power-list iteration. Do not apply a blockable Strength debuff.
        if (desired == 0) {
            this.owner.powers.remove(strength);
            strength.onRemove();
        } else if (strength == null) {
            strength = new StrengthPower(this.owner, desired);
            strength.amount = desired;
            strength.updateDescription();
            this.owner.powers.add(strength);
            Collections.sort(this.owner.powers);
        } else {
            if (strength.amount == desired) {
                return false;
            }
            strength.amount = desired;
            strength.updateDescription();
        }
        return true;
    }
}
