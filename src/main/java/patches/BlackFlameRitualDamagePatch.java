package patches;

import actions.BloodlossReduceMaxHealthAction;
import actions.GreyHealthLoseHpAction;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.BlackFlameRitualPower;

import java.util.ArrayDeque;

@SpirePatch(clz = AbstractMonster.class, method = "damage", paramtypez = { DamageInfo.class })
public class BlackFlameRitualDamagePatch {
    private static final ArrayDeque<DamageFrame> FRAMES = new ArrayDeque<>();

    private static class DamageFrame {
        final AbstractMonster monster;
        final int healthBefore;
        final BlackFlameRitualPower power;
        int nestedLoss;

        DamageFrame(AbstractMonster monster) {
            this.monster = monster;
            this.healthBefore = Math.max(0, monster.currentHealth);
            AbstractPower active = AbstractDungeon.player == null ? null
                    : AbstractDungeon.player.getPower(BlackFlameRitualPower.POWER_ID);
            this.power = active instanceof BlackFlameRitualPower ? (BlackFlameRitualPower)active : null;
        }
    }

    @SpirePrefixPatch
    public static void prefix(AbstractMonster __instance, DamageInfo info) {
        beforeLoss(__instance);
    }

    @SpirePostfixPatch
    public static void postfix(AbstractMonster __instance, DamageInfo info) {
        afterLoss(__instance);
    }

    private static void beforeLoss(AbstractCreature target) {
        if (target instanceof AbstractMonster) {
            FRAMES.push(new DamageFrame((AbstractMonster)target));
        }
    }

    private static void afterLoss(AbstractCreature target) {
        if (!(target instanceof AbstractMonster)) {
            return;
        }
        AbstractMonster monster = (AbstractMonster)target;
        DamageFrame frame = FRAMES.pop();
        int loss = Math.max(0, frame.healthBefore - Math.max(0, monster.currentHealth));
        // A nested damage call on this same monster must not be counted by its parent twice.
        for (DamageFrame parent : FRAMES) {
            if (parent.monster == monster) {
                parent.nestedLoss += loss;
                break;
            }
        }
        if (frame.power != null) {
            frame.power.onEnemyLoseHp(monster, Math.max(0, loss - frame.nestedLoss));
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "decreaseMaxHealth", paramtypez = { int.class })
    public static class MaxHealthLoss {
        @SpirePrefixPatch
        public static void prefix(AbstractCreature __instance, int amount) {
            beforeLoss(__instance);
        }

        @SpirePostfixPatch
        public static void postfix(AbstractCreature __instance, int amount) {
            afterLoss(__instance);
        }
    }

    @SpirePatch(clz = BloodlossReduceMaxHealthAction.class, method = "update")
    public static class BloodlossMaxHealthLoss {
        @SpirePrefixPatch
        public static void prefix(BloodlossReduceMaxHealthAction __instance) {
            beforeLoss(__instance.target);
        }

        @SpirePostfixPatch
        public static void postfix(BloodlossReduceMaxHealthAction __instance) {
            afterLoss(__instance.target);
        }
    }

    @SpirePatch(clz = GreyHealthLoseHpAction.class, method = "loseHpDirectly",
            paramtypez = { AbstractCreature.class, int.class })
    public static class DirectHealthLoss {
        @SpirePrefixPatch
        public static void prefix(AbstractCreature target, int amount) {
            beforeLoss(target);
        }

        @SpirePostfixPatch
        public static void postfix(AbstractCreature target, int amount) {
            afterLoss(target);
        }
    }
}
