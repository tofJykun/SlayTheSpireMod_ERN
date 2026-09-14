package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import powers.HandAxeReturnPower;

import java.util.UUID;

public class TrackHandAxeReturnAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final UUID cardUuid;

    public TrackHandAxeReturnAction(AbstractPlayer player, UUID cardUuid) {
        this.player = player;
        this.cardUuid = cardUuid;
        this.actionType = ActionType.POWER;
    }

    @Override
    public void update() {
        if (this.player == null || this.cardUuid == null) {
            this.isDone = true;
            return;
        }

        if (this.player.hasPower(HandAxeReturnPower.POWER_ID)
                && this.player.getPower(HandAxeReturnPower.POWER_ID) instanceof HandAxeReturnPower) {
            ((HandAxeReturnPower)this.player.getPower(HandAxeReturnPower.POWER_ID)).addCard(this.cardUuid);
        } else {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.player, this.player,
                    new HandAxeReturnPower(this.player, this.cardUuid), 1));
        }
        this.isDone = true;
    }
}
