package powers;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import general.ConciliationRewards;

public class FlipPower extends AbstractPower {
    public static final String POWER_ID = "FlipPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private AbstractRoom victoryRoom;
    private boolean victoryRecorded;
    private int pendingRewards;

    public FlipPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateDescription();
    }

    @Override
    public void onVictory() {
        if (victoryRecorded) {
            return;
        }
        victoryRecorded = true;
        victoryRoom = AbstractDungeon.getCurrRoom();
        // endBattle clears the action manager (and resets turn to 1) after onVictory.
        if (victoryRoom != null && !victoryRoom.smoked && !ConciliationRewards.suppressCards(victoryRoom)
                && GameActionManager.turn > 0 && GameActionManager.turn % 2 == 1) {
            pendingRewards = Math.max(0, amount);
        }
    }

    public int consumeRewards(AbstractRoom room) {
        int count = pendingRewards;
        pendingRewards = 0;
        if (room == null || room != victoryRoom || !room.isBattleOver
                || room.smoked || ConciliationRewards.suppressCards(room)) {
            return 0;
        }
        return count;
    }

    @Override
    public void updateDescription() {
        this.description = STRINGS.DESCRIPTIONS[0] + this.amount + STRINGS.DESCRIPTIONS[1];
    }
}
