package general;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.VirtualCurrencyPower;

public final class GoldPaymentHelper {
    private GoldPaymentHelper() {}

    public static long available(AbstractPlayer player) {
        if (player == null) return 0;
        AbstractPower tokens = player.getPower(VirtualCurrencyPower.POWER_ID);
        return (long)Math.max(0, player.gold)
                + (tokens instanceof VirtualCurrencyPower ? Math.max(0, tokens.amount) : 0);
    }
}
