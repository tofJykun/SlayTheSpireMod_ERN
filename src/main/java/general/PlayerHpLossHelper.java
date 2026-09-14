package general;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import powers.PowerOfHouseMaraisPower;
import powers.PowerOfVengeancePower;

public class PlayerHpLossHelper {
    private PlayerHpLossHelper() {
    }

    public static void onPlayerLostHp(AbstractPlayer player, int lostHp) {
        if (player == null || lostHp <= 0) {
            return;
        }
        PowerOfVengeancePower.onPlayerLostHp(player, lostHp);
        PowerOfHouseMaraisPower.onPlayerLostHp(player, lostHp);
    }
}
