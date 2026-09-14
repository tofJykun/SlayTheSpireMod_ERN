package patches;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import powers.PumpkinHelmPower;
import powers.PowerOfVengeancePower;
import powers.SummonFrederick;
import powers.UndeathPower;

public class PumpkinHelmBlockPatch {
    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class RetainLimitedBlockWithUndeath {
        @SpireInsertPatch(locator = Locator.class)
        public static void insert(GameActionManager __instance) {
            UndeathPower.retainBlockAtTurnStart(AbstractDungeon.player);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasPower");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class RetainFrederickBlockLikeBarricade {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if ("com.megacrit.cardcrawl.characters.AbstractPlayer".equals(m.getClassName())
                            && "hasPower".equals(m.getMethodName())) {
                        m.replace("$_ = $proceed($$) || (\"Barricade\".equals($1) && "
                                + "com.megacrit.cardcrawl.dungeons.AbstractDungeon.player != null && ("
                                + "com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasPower(\""
                                + UndeathPower.POWER_ID + "\") || "
                                + "powers.PowerOfVengeancePower.hasPowerOfVengeance("
                                + "com.megacrit.cardcrawl.dungeons.AbstractDungeon.player) || ("
                                + "com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasPower(\""
                                + PumpkinHelmPower.POWER_ID + "\") && "
                                + "com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasPower(\""
                                + SummonFrederick.POWER_ID + "\"))));");
                    }
                }
            };
        }
    }
}
