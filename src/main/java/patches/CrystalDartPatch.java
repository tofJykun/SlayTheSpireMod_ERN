package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.BlockReturnPower;
import general.CrystalDartCombat;

public class CrystalDartPatch {
    @SpirePatch(clz = DamageAction.class, method = "update")
    @SpirePatch(clz = VampireDamageAction.class, method = "update")
    public static class AttackTarget {
        @SpirePrefixPatch
        public static void prefix(AbstractGameAction __instance, @ByRef DamageInfo[] ___info) {
            AbstractMonster target = CrystalDartCombat.target(__instance.target, ___info[0]);
            if (target == null) return;
            __instance.target = target;
            ___info[0] = CrystalDartCombat.damageFor(___info[0], target);
        }
    }

    // Also covers attacks implemented by specialized actions or direct damage calls.
    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class DirectAttack {
        @SpirePrefixPatch
        public static SpireReturn<Void> prefix(AbstractPlayer __instance, DamageInfo info) {
            AbstractMonster target = CrystalDartCombat.target(__instance, info);
            if (target == null) return SpireReturn.Continue();
            target.damage(CrystalDartCombat.damageFor(info, target));
            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = BlockReturnPower.class, method = "onAttacked")
    public static class AttackerReceivesBlock {
        @SpirePrefixPatch
        public static SpireReturn<Integer> prefix(BlockReturnPower __instance, DamageInfo info, int damageAmount) {
            if (__instance.owner instanceof AbstractMonster && info != null
                    && info.owner instanceof AbstractMonster && info.owner != __instance.owner
                    && info.type == DamageInfo.DamageType.NORMAL) {
                __instance.flash();
                AbstractDungeon.actionManager.addToTop(new GainBlockAction(info.owner, __instance.amount, Settings.FAST_MODE));
                return SpireReturn.Return(damageAmount);
            }
            return SpireReturn.Continue();
        }
    }
}
