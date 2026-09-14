package patches;

import actions.StargazeTopDeckAction;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.actions.defect.ShuffleAllAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import powers.StargazePower;

public class StargazeShufflePatch {
    @SpirePatch(clz = ShuffleAction.class, method = "update")
    public static class ShuffleActionPatch {
        @SpirePostfixPatch
        public static void postfix(ShuffleAction __instance) {
            Boolean triggerRelics = ReflectionHacks.getPrivate(__instance, ShuffleAction.class, "triggerRelics");
            if (__instance.isDone && triggerRelics != null && triggerRelics.booleanValue()) {
                triggerStargaze();
            }
        }
    }

    @SpirePatch(clz = EmptyDeckShuffleAction.class, method = "update")
    public static class EmptyDeckShufflePatch {
        @SpirePostfixPatch
        public static void postfix(EmptyDeckShuffleAction __instance) {
            if (__instance.isDone) {
                triggerStargaze();
            }
        }
    }

    @SpirePatch(clz = ShuffleAllAction.class, method = "update")
    public static class ShuffleAllPatch {
        @SpirePostfixPatch
        public static void postfix(ShuffleAllAction __instance) {
            if (__instance.isDone) {
                triggerStargaze();
            }
        }
    }

    private static void triggerStargaze() {
        if (AbstractDungeon.getCurrRoom() == null
                || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT
                || AbstractDungeon.player == null
                || !AbstractDungeon.player.hasPower(StargazePower.POWER_ID)) {
            return;
        }

        AbstractPower power = AbstractDungeon.player.getPower(StargazePower.POWER_ID);
        if (power != null && power.amount > 0) {
            power.flash();
            AbstractDungeon.actionManager.addToTop((AbstractGameAction)new StargazeTopDeckAction(power.amount));
        }
    }
}
