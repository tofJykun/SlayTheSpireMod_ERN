package general;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import powers.SnakeSloughPower;
import relics.AldiaFund;

public final class TransformStats {
    private static int combatTransforms = 0;

    private TransformStats() {
    }

    public static void resetCombat() {
        combatTransforms = 0;
    }

    public static void recordTransform() {
        if (CombatState.isInCombat()) {
            combatTransforms++;
            triggerTransformPowers();
            triggerTransformRelics();
        }
    }

    public static int getCombatTransforms() {
        return combatTransforms;
    }

    private static void triggerTransformPowers() {
        if (AbstractDungeon.player == null || !AbstractDungeon.player.hasPower(SnakeSloughPower.POWER_ID)) {
            return;
        }
        AbstractPower power = AbstractDungeon.player.getPower(SnakeSloughPower.POWER_ID);
        if (power instanceof SnakeSloughPower) {
            ((SnakeSloughPower)power).onTransformCard();
        }
    }

    private static void triggerTransformRelics() {
        if (AbstractDungeon.player == null || !AbstractDungeon.player.hasRelic(AldiaFund.ID)) {
            return;
        }
        AbstractRelic relic = AbstractDungeon.player.getRelic(AldiaFund.ID);
        if (relic instanceof AldiaFund) {
            ((AldiaFund)relic).onTransformCard();
        }
    }
}
