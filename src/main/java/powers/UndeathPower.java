package powers;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class UndeathPower extends AbstractPower {
    public static final String POWER_ID = "UndeathPower";
    public static final int RETAIN_BLOCK = 10;
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public UndeathPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        updateDescription();
        PowerIconHelper.load(this, POWER_ID);
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount = -1;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + RETAIN_BLOCK + DESCRIPTIONS[1];
    }

    public static void retainBlockAtTurnStart(AbstractPlayer player) {
        if (player == null || !player.hasPower(POWER_ID) || player.currentBlock <= 0 || hasFullBlockRetention(player)) {
            return;
        }

        int retainedBlock = Math.min(player.currentBlock, RETAIN_BLOCK);
        if (player.hasRelic("Calipers")) {
            retainedBlock += Math.max(0, player.currentBlock - RETAIN_BLOCK - 15);
        }

        int blockLoss = player.currentBlock - retainedBlock;
        if (blockLoss > 0) {
            player.loseBlock(blockLoss);
        }
    }

    private static boolean hasFullBlockRetention(AbstractPlayer player) {
        return player.hasPower("Barricade") || player.hasPower("Blur")
                || (player.hasPower(PumpkinHelmPower.POWER_ID) && player.hasPower(SummonFrederick.POWER_ID));
    }
}

