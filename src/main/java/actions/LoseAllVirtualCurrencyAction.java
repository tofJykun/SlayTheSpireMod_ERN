package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.VirtualCurrencyPower;

public class LoseAllVirtualCurrencyAction extends AbstractGameAction {
    private final AbstractPlayer player;

    public LoseAllVirtualCurrencyAction(AbstractPlayer player) {
        this.player = player;
        this.actionType = ActionType.POWER;
    }

    @Override
    public void update() {
        if (this.isDone) {
            return;
        }
        this.isDone = true;
        AbstractPower tokens = this.player == null ? null : this.player.getPower(VirtualCurrencyPower.POWER_ID);
        if (tokens instanceof VirtualCurrencyPower) {
            // Use the normal loss hook so Loyalty Card receives the actual amount lost.
            ((VirtualCurrencyPower)tokens).loseTokens(tokens.amount);
        }
    }
}
